package org.codehaus.werkflow;

public class DuplicateWorkflowException
    extends WerkflowException
{
    private Workflow workflow;

    public DuplicateWorkflowException(Workflow workflow)
    {
        this.workflow = workflow;
    }

    public Workflow getWorkflow()
    {
        return this.workflow;
    }

    public String getMessage()
    {
        return "Duplicate workflow with id <" + getWorkflow().getId() + ">";
    }
}
