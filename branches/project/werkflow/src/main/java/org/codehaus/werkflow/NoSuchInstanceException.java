package org.codehaus.werkflow;

public class NoSuchInstanceException
    extends WerkflowException
{
    private String id;

    public NoSuchInstanceException(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public String getMessage()
    {
        return "No instance with id <" + getId() + ">";
    }
}
