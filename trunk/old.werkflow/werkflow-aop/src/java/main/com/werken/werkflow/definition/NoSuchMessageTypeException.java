package com.werken.werkflow.definition;

import com.werken.werkflow.WerkflowException;

public class NoSuchMessageTypeException
    extends WerkflowException
{
    private String id;

    public NoSuchMessageTypeException(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public String getMessage()
    {
        return "no such message type: " + getId();
    }
}
