package com.werken.werkflow.engine;

import com.werken.werkflow.Attributes;
import com.werken.werkflow.WfmsRuntime;
import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.NoSuchProcessException;
import com.werken.werkflow.NoSuchCaseException;
import com.werken.werkflow.activity.Activity;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.resource.Resource;
import com.werken.werkflow.service.WfmsServices;
import com.werken.werkflow.service.caserepo.CaseState;
import com.werken.werkflow.work.WorkItem;

import java.util.Map;
import java.util.HashMap;

public class WorkflowRuntime
    implements WfmsRuntime
{
    private WorkflowEngine engine;
    
    public WorkflowRuntime(WorkflowEngine engine)
    {
        this.engine = engine;
    }

    public WorkflowEngine getEngine()
    {
        return this.engine;
    }

    public ProcessInfo[] getProcesses()
    {
        return getEngine().getProcesses();
    }

    public ProcessInfo getProcess(String processId)
        throws NoSuchProcessException
    {
        return getEngine().getProcess( processId );
    }

    public ProcessCase getProcessCase(String caseId)
        throws NoSuchProcessException, NoSuchCaseException
    {
        return getEngine().getProcessCase( caseId );
    }

    public ProcessCase newProcessCase(String processId,
                                      Attributes attributes)
        throws NoSuchProcessException
    {
        return getEngine().newProcessCase( processId,
                                           attributes );
    }

    public Resource[] getResources()
    {
        return null;
    }

    public WorkItem[] getWorkItemsForProcessCase(String caseId)
        throws NoSuchCaseException, NoSuchProcessException
    {
        return getEngine().getWorkItemsForProcessCase( caseId );
    }

    public WorkItem[] getWorkItemsForResource(String resourceId)
    {
        return null;
    }

    public Activity[] getActivitiesForProcessCase(String caseId)
        throws NoSuchCaseException, NoSuchProcessException
    {
        return getEngine().getActivitiesForProcessCase( caseId );
    }
}
