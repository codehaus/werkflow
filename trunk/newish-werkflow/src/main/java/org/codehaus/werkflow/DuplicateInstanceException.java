package org.codehaus.werkflow;

public class DuplicateInstanceException
    extends WerkflowException
{
    private Instance instance;

    public DuplicateInstanceException(Instance instance)
    {
        this.instance = instance;
    }

    public Instance getInstance()
    {
        return this.instance;
    }

    public String getMessage()
    {
        return "Duplicate instance with id <" + getInstance().getId() + ">";
    }
}
