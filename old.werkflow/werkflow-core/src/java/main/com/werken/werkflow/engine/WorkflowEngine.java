package com.werken.werkflow.engine;

/*
 $Id$

 Copyright 2003 (C) The Werken Company. All Rights Reserved.
 
 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.
 
 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.
 
 3. The name "werkflow" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.
 
 4. Products derived from this Software may not be called "werkflow"
    nor may "werkflow" appear in their names without prior written
    permission of The Werken Company. "werkflow" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://werkflow.werken.com/).
 
 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.
 
 */

import com.werken.werkflow.Attributes;
import com.werken.werkflow.Wfms;
import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.WfmsRuntime;
import com.werken.werkflow.SimpleAttributes;
import com.werken.werkflow.NoSuchCaseException;
import com.werken.werkflow.NoSuchProcessException;
import com.werken.werkflow.QueryException;
import com.werken.werkflow.action.Action;
import com.werken.werkflow.activity.Activity;
import com.werken.werkflow.admin.WfmsAdmin;
import com.werken.werkflow.admin.ProcessException;
import com.werken.werkflow.admin.DuplicateProcessException;
import com.werken.werkflow.admin.ProcessVerificationException;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.log.Log;
import com.werken.werkflow.log.SimpleLog;
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

/** Core implementation of <code>Wfms</code>.
 *
 *  <p>
 *  An instance of <code>WorkflowEngine</code> may be instantiated
 *  either directly or via the <code>PlexusWfms</code> avalon-plexus
 *  component.
 *  </p>
 *
 *  <p>
 *  To assist the <code>WorkflowEngine</code>, a configured <code>WfmsServices</code>
 *  should be passed in during construction.  This allows the engine to access
 *  the case storage repository and messaging manager at runtime.
 *  </p>
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class WorkflowEngine
    implements Wfms
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Log sink. */
    private Log log;

    /** External services. */
    private WfmsServices services;

    /** Admin interface implementation. */
    private WfmsAdmin admin;

    /** Runtime interface implementation. */
    private WfmsRuntime runtime;

    /** Resource manager. */
    private ResourceManager resourceManager;

    /** Activity-execution manager. */
    private ActivityManager activityManager;

    /** Work-item manager. */
    private WorkItemManager workItemManager;

    /** Deployed processes. */
    private Map deployments;

    /** Currently paged-in cases. */
    private Map cases;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  <p>
     *  Create an engine that uses its own simple logging mechanism.
     *  </p>
     *
     *  @param services The runtime external services.
     */
    public WorkflowEngine(WfmsServices services)
    {
        this( services,
              new SimpleLog( "WorkflowEngine" ) );
    }

    /** Construct.
     *
     *  @param services The runtime external services.
     *  @param log The root log sink.
     */
    public WorkflowEngine(WfmsServices services,
                          Log log)
    {
        this.log      = log;
        this.services = services;

        this.resourceManager = new ResourceManager( this );
        this.activityManager = new ActivityManager( this );
        this.workItemManager = new WorkItemManager( this );

        this.admin   = new WorkflowAdmin( this );
        this.runtime = new WorkflowRuntime( this );

        this.deployments = new HashMap();
        this.cases       = new HashMap();
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Retrieve the log sink.
     *
     *  @return The log sink.
     */
    Log getLog()
    {
        return this.log;
    }

    /** Retrieve the external services broker.
     *
     *  @return The external services broker.
     */
    WfmsServices getServices()
    {
        return this.services;
    }

    /** @see Wfms
     */
    public WfmsRuntime getRuntime()
    {
        return this.runtime;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     com.werken.werkflow.WfmsRuntime
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the <code>ProcessInfo</code> descriptors for
     *  all available deployed processes.
     *
     *  @return The process-info descriptors.
     */
    ProcessInfo[] getProcesses()
    {
        return (ProcessInfo[]) this.deployments.values().toArray( ProcessInfo.EMPTY_ARRAY );
    }

    /** Retrieve the <code>ProcessInfo</code> descriptor for
     *  a specific process.
     *
     *  @param processId The process id.
     *
     *  @return The process-info descriptor.
     *
     *  @throws NoSuchProcessException If the <code>processId</code>
     *          does not refer to a valid deployed process.
     */
    ProcessInfo getProcess(String processId)
        throws NoSuchProcessException
    {
        return getProcessDeployment( processId );
    }
    
    /** Create a new <code>ProcessCase</code> for a particular process.
     *
     *  @param processId The id of the process.
     *  @param attributes The initial attributes for the case.
     *
     *  @return The newly created process case.
     *
     *  @throws NoSuchProcessException If the process identifier does
     *          not refer to a currently deployed process definition.
     */
    WorkflowProcessCase newProcessCase(String processId,
                                       Attributes attributes)
        throws NoSuchProcessException
    {
        CaseState caseState = newCaseState( processId,
                                            attributes );

        return assumeCase( caseState );
    }

    /** Retrieve a <code>ProcessCase</code> by its id.
     *
     *  @param caseId The case id.
     *
     *  @return The case associated with the id.
     *
     *  @throws NoSuchCaseException If the identifier does not
     *          match any case known by the system.
     *  @throws NoSuchProcessException If the identifier do match
     *          a case known by the system, but the process associated
     *          with the case is not currently deployed.
     */
    WorkflowProcessCase getProcessCase(String caseId)
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

    ProcessCase[] selectCases(String processId,
                              String placeId)
        throws QueryException
    {
        String[] caseIds = getServices().getCaseRepository().selectCases( processId,
                                                                          placeId );
        
        ProcessCase[] cases = new ProcessCase[ caseIds.length ];
        
        try
        {
            for ( int i = 0 ; i < caseIds.length ; ++i )
            {
                cases[i] = getProcessCase( caseIds[i] );
            }
        }
        catch (NoSuchCaseException e)
        {
            throw new QueryException( e );
        }
        catch (NoSuchProcessException e)
        {
            throw new QueryException( e );
        }
        
        return cases;
    }

    ProcessCase[] selectCases(String processId,
                              Map qbeAttrs)
        throws QueryException
    {
        String[] caseIds = getServices().getCaseRepository().selectCases( processId,
                                                                          qbeAttrs );
        
        ProcessCase[] cases = new ProcessCase[ caseIds.length ];
        
        try
        {
            for ( int i = 0 ; i < caseIds.length ; ++i )
            {
                cases[i] = getProcessCase( caseIds[i] );
            }
        }
        catch (NoSuchCaseException e)
        {
            throw new QueryException( e );
        }
        catch (NoSuchProcessException e)
        {
            throw new QueryException( e );
        }
            
        return cases;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     com.werken.werkflow.admin.WfmsAdmin
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** @see Wfms
     */
    public WfmsAdmin getAdmin()
    {
        return this.admin;
    }

    /** Deploy a <code>ProcessDefinition</code> making it available
     *  for case creation and manipulation.
     *
     *  @param processDef The process definition to deploy.
     *
     *  @throws ProcessException If an error occurs while attempting to
     *          deploy the process.
     */
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

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Assume management control of a <code>CaseState</code>.
     *
     *  @param caseState The case-state to assume control of.
     *
     *  @throws NoSuchProcessException If the case-state is associated with
     *          a process not currently deployed within the engine.
     */
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

    /** Perform pre-deployment verification of a <code>ProcessDefinition</code>.
     *
     *  @param processDef The process-definition to verify.
     *
     *  @throws ProcessVerificationException If the process definition does not
     *          pass the verification procedures.
     */
    void verifyProcess(ProcessDefinition processDef)
        throws ProcessVerificationException
    {
        //verifyNet( processDef );
        //verifyResources( processDef );
    }

    /** Deploy an verified <code>ProcessDefinition</code>.
     *
     *  @param processDef The process-definition to deploy.
     *
     *  @throws ProcessException If an error occurs while attempting to
     *          deploy the process.
     */
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

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     com.werken.werkflow.service.messaging.MessagingManager
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Register a <code>MessageSink</code> as interested in messages
     *  of a particular <code>MessageType</code>.
     *
     *  @see com.werken.werkflow.service.messaging.MessagingManager
     *
     *  @param messageSink The message sink.
     *  @param messageType The message-type of interest.
     *
     *  @throws IncompatibleMessageSelectorException If the message-type
     *          contains a message-selector incompatible with the underlying
     *          messaging-manager.
     */
    Registration register(MessageSink messageSink,
                          MessageType messageType)
        throws IncompatibleMessageSelectorException
    {
        return getServices().getMessagingManager().register( messageSink,
                                                             messageType );
    }

    /** Retrieve the <code>ProcessDeployemtn</code> for
     *  a specific process.
     *
     *  @param processId The process id.
     *
     *  @return The process-deployment.
     *
     *  @throws NoSuchProcessException If the <code>processId</code>
     *          does not refer to a valid deployed process.
     */
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

    /** Evaluate the current state of a <code>WorkflowProcessCase</code>.
     *
     *  @param processCase The process-case to evaluate.
     *
     *  @throws NoSuchProcessException If the case-state is associated with
     *          a process not currently deployed within the engine.
     */
    void evaluateCase(WorkflowProcessCase processCase)
        throws NoSuchProcessException
    {
        ProcessDeployment deployment = getProcessDeployment( processCase.getProcessInfo().getId() );

        deployment.evaluateCase( processCase );

        getActivityManager().scheduleCase( processCase );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the <code>ActivityManager</code>.
     *
     *  @return The activity-manager.
     */
    protected ActivityManager getActivityManager()
    {
        return this.activityManager;
    }

    /** Retrieve the <code>ResourceManager</code>.
     *
     *  @return The activity-manager.
     */
    protected ResourceManager getResourceManager()
    {
        return this.resourceManager;
    }

    /** Retrieve the <code>WorkItemmanager</code>.
     *
     *  @return The activity-manager.
     */
    protected WorkItemManager getWorkItemManager()
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
