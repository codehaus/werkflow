package org.codehaus.werkflow;

public class NoSuchWorkflowException
    extends WerkflowException
{
    private String id;

    public NoSuchWorkflowException(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public String getMessage()
    {
        return "No workflow with id <" + getId() + ">";
    }
}
