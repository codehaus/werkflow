package org.codehaus.werkflow.helpers;

import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.NoSuchWorkflowException;
import org.codehaus.werkflow.spi.WorkflowManager;

import java.util.Map;
import java.util.HashMap;

public class SimpleWorkflowManager
    implements WorkflowManager
{
    private Map workflows;

    public SimpleWorkflowManager()
    {
        this.workflows = new HashMap();
    }

    public void addWorkflow(Workflow workflow)
    {
        this.workflows.put( workflow.getId(),
                            workflow );
    }

    public Workflow getWorkflow(String id)
        throws NoSuchWorkflowException
    {
        if ( ! this.workflows.containsKey( id ) )
        {
            throw new NoSuchWorkflowException( id );
        }
        return (Workflow) this.workflows.get( id );
    }

    public Workflow[] getWorkflows()
    {
        return (Workflow[]) this.workflows.values().toArray( new Workflow[ this.workflows.size() ] );
    }
}
