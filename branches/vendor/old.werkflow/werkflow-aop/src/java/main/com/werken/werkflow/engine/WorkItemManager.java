package com.werken.werkflow.engine;

import com.werken.werkflow.NoSuchCaseException;
import com.werken.werkflow.NoSuchProcessException;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.resource.ResourceClass;
import com.werken.werkflow.resource.NoSuchResourceClassException;
import com.werken.werkflow.task.Task;
import com.werken.werkflow.task.ResourceSpec;
import com.werken.werkflow.work.WorkItem;

import java.util.List;
import java.util.ArrayList;

public class WorkItemManager
{
    private WorkflowEngine engine;

    public WorkItemManager(WorkflowEngine engine)
    {
        this.engine = engine;
    }

    protected WorkflowEngine getEngine()
    {
        return this.engine;
    }

    public WorkItem[] getWorkItemsForProcessCase(String caseId)
        throws NoSuchCaseException, NoSuchProcessException
    {
        List workItems = new ArrayList();

        WorkflowProcessCase processCase = getEngine().getProcessCase( caseId );

        Transition[] transitions = processCase.getEnabledTransitions();

        for ( int i = 0 ; i < transitions.length ; ++i )
        {
            Task task = transitions[i].getTask();
            
            try
            {
                if ( requiresOfflineResource( task ) )
                {
                    workItems.add( new WorkflowWorkItem( caseId,
                                                         transitions[i].getId() ) );
                }
            }
            catch (NoSuchResourceClassException e)
            {
                // FIXME
                e.printStackTrace();
            }
        }
        
        return null;
    }

    protected boolean requiresOfflineResource(Task task)
        throws NoSuchResourceClassException
    {
        ResourceSpec[] resourceSpecs = task.getResourceSpecs();

        for ( int i = 0 ; i < resourceSpecs.length ; ++i )
        {
            if ( requiresOfflineResource( resourceSpecs[i] ) )
            {
                return true;
            }
        }

        return false;
    }

    protected boolean requiresOfflineResource(ResourceSpec resourceSpec)
        throws NoSuchResourceClassException
    {
        String[] resourceClassIds = resourceSpec.getResourceClassIds();

        for ( int i = 0 ; i < resourceClassIds.length ; ++i )
        {
            if ( engine.getResourceClass( resourceClassIds[i] ).isOffline() )
            {
                return true;
            }
        }

        return false;
    }
}
