package com.werken.werkflow.definition;

import com.werken.werkflow.WerkflowException;

public class DuplicateActionException
    extends WerkflowException
{
    private String id;

    public DuplicateActionException(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public String getMessage()
    {
        return "duplicate action: " + getId();
    }
}
