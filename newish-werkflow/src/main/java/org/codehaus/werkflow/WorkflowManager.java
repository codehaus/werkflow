package org.codehaus.werkflow;

public interface WorkflowManager
{
    Workflow getWorkflow(String id)
        throws NoSuchWorkflowException;
}
