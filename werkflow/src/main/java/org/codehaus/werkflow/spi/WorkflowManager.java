package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.NoSuchWorkflowException;

public interface WorkflowManager
{
    Workflow getWorkflow(String id)
        throws NoSuchWorkflowException;

    Workflow[] getWorkflows();
}
