package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.NoSuchWorkflowException;

public interface WorkflowManager
{
    void addWorkflow(Workflow workflow);
    
    Workflow getWorkflow(String id)
        throws NoSuchWorkflowException;

    Workflow[] getWorkflows();
}
