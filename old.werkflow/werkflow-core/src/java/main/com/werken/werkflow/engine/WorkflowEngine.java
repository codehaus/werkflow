package com.werken.werkflow.engine;

import com.werken.werkflow.Attributes;
import com.werken.werkflow.Wfms;
import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.WfmsRuntime;
import com.werken.werkflow.NoSuchCaseException;
import com.werken.werkflow.NoSuchProcessException;
import com.werken.werkflow.activity.Activity;
import com.werken.werkflow.admin.WfmsAdmin;
import com.werken.werkflow.admin.ProcessException;
import com.werken.werkflow.admin.DuplicateProcessException;
import com.werken.werkflow.admin.ProcessVerificationException;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.resource.ResourceClass;
import com.werken.werkflow.resource.DuplicateResourceClassException;
import com.werken.werkflow.resource.NoSuchResourceClassException;
import com.werken.werkflow.service.WfmsServices;
import com.werken.werkflow.service.caserepo.CaseState;
import com.werken.werkflow.service.messaging.MessageSink;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.IncompatibleMessageSelectorException;
import com.werken.werkflow.task.Task;
import com.werken.werkflow.work.WorkItem;

import java.util.Map;
import java.util.HashMap;

public class WorkflowEngine
    implements Wfms
{
    private WfmsServices services;
    private WfmsAdmin admin;
    private WfmsRuntime runtime;

    private ResourceManager resourceManager;
    private ActivityManager activityManager;
    private WorkItemManager workItemManager;

    private Map deployments;

    private Map cases;

    public WorkflowEngine(WfmsServices services)
    {
        this.services = services;

        this.resourceManager = new ResourceManager( this );
        this.activityManager = new ActivityManager( this );
        this.workItemManager = new WorkItemManager( this );

        this.admin   = new WorkflowAdmin( this );
        this.runtime = new WorkflowRuntime( this );

        this.deployments = new HashMap();
        this.cases       = new HashMap();
    }

    WfmsServices getServices()
    {
        return this.services;
    }

    public ProcessInfo[] getProcesses()
    {
        return (ProcessInfo[]) this.deployments.values().toArray( ProcessInfo.EMPTY_ARRAY );
    }

    public ProcessInfo getProcess(String processId)
        throws NoSuchProcessException
    {
        return getProcessDeployment( processId );
    }
    
    public WorkflowProcessCase newProcessCase(String processId,
                                              Attributes attributes)
        throws NoSuchProcessException
    {
        CaseState caseState = newCaseState( processId,
                                            attributes );

        return assumeCase( caseState );
    }

    public WorkflowProcessCase getProcessCase(String caseId)
        throws NoSuchCaseException, NoSuchProcessException
    {
        if ( this.cases.containsKey( caseId ) )
        {
            return (WorkflowProcessCase) this.cases.get( caseId );
        }

        CaseState caseState = getCaseState( caseId );

        if ( caseState == null )
        {
            throw new NoSuchCaseException( caseId );
        }

        return assumeCase( caseState );
    }

    WorkflowProcessCase assumeCase(CaseState caseState)
        throws NoSuchProcessException
    {
        ProcessInfo info = getProcess( caseState.getProcessId() );

        WorkflowProcessCase processCase = new WorkflowProcessCase( info,
                                                                   caseState );

        this.cases.put( processCase.getId(),
                        processCase );

        evaluateCase( processCase );

        return processCase;
    }

    public WfmsRuntime getRuntime()
    {
        return this.runtime;
    }

    public WfmsAdmin getAdmin()
    {
        return this.admin;
    }

    void deployProcess(ProcessDefinition processDef)
        throws ProcessException
    {
        String processId = processDef.getId();

        if ( this.deployments.containsKey( processId ) )
        {
            throw new DuplicateProcessException( processDef );
        }

        verifyProcess( processDef );

        deployVerifiedProcess( processDef );
    }

    void verifyProcess(ProcessDefinition processDef)
        throws ProcessVerificationException
    {
        //verifyNet( processDef );
        //verifyResources( processDef );
    }

    void deployVerifiedProcess(ProcessDefinition processDef)
        throws ProcessException
    {
        try
        {
            ProcessDeployment deployment = new ProcessDeployment( this,
                                                                  processDef );
            
            this.deployments.put( processDef.getId(),
                                  deployment );
        }
        catch (ProcessDeploymentException e)
        {
            throw new ProcessException( processDef,
                                        e );
        }
    }

    Registration register(MessageSink messageSink,
                          MessageType messageType)
        throws IncompatibleMessageSelectorException
    {
        return getServices().getMessagingManager().register( messageSink,
                                                             messageType );
    }

    ProcessDeployment getProcessDeployment(String processId)
        throws NoSuchProcessException
    {
        ProcessDeployment deployment = (ProcessDeployment) this.deployments.get( processId );

        if ( deployment == null )
        {
            throw new NoSuchProcessException( processId );
        }

        return deployment;
    }

    void evaluateCase(WorkflowProcessCase processCase)
        throws NoSuchProcessException
    {
        ProcessDeployment deployment = getProcessDeployment( processCase.getProcessInfo().getId() );

        deployment.evaluateCase( processCase );

        getActivityManager().scheduleCase( processCase );
    }

    ActivityManager getActivityManager()
    {
        return this.activityManager;
    }

    ResourceManager getResourceManager()
    {
        return this.resourceManager;
    }

    WorkItemManager getWorkItemManager()
    {
        return this.workItemManager;
    }

    ResourceClass getResourceClass(String id)
        throws NoSuchResourceClassException
    {
        return getResourceManager().getResourceClass( id );
    }

    void addResourceClass(ResourceClass resourceClass)
        throws DuplicateResourceClassException
    {
        getResourceManager().addResourceClass( resourceClass );
    }

    Activity[] getActivitiesForProcessCase(String caseId)
        throws NoSuchCaseException, NoSuchProcessException
    {
        return getActivityManager().getActivitiesForProcessCase( caseId );
    }

    WorkItem[] getWorkItemsForProcessCase(String caseId)
        throws NoSuchCaseException, NoSuchProcessException
    {
        return getWorkItemManager().getWorkItemsForProcessCase( caseId );
    }

    CaseState newCaseState(String processId,
                           Attributes attributes)
    {
        return getServices().getCaseRepository().newCaseState( processId,
                                                               attributes );
    }

    CaseState getCaseState(String caseId)
    {
        return getServices().getCaseRepository().getCaseState( caseId );
    }

    Object consumeMessage(WorkflowProcessCase processCase,
                           Transition transition)
        throws NoSuchCorrelationException, NoSuchProcessException
    {
        ProcessDeployment deployment = getProcessDeployment( processCase.getProcessInfo().getId() );
        
        return deployment.consumeMessage( processCase.getId(),
                                          transition );
    }
}
