package org.codehaus.werkflow;

public class DuplicateInstanceException
    extends WerkflowException
{
    private String id;

    public DuplicateInstanceException(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public String getMessage()
    {
        return "Duplicate instance with id [" + getId() + "]";
    }
}
