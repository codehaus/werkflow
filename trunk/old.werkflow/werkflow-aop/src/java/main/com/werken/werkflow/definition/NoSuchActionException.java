package com.werken.werkflow.definition;

import com.werken.werkflow.WerkflowException;

public class NoSuchActionException
    extends WerkflowException
{
    private String id;

    public NoSuchActionException(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public String getMessage()
    {
        return "no such action: " + getId();
    }
}
