package com.werken.werkflow.resource;

import com.werken.werkflow.WerkflowException;

public class ResourceClassException
    extends WerkflowException
{
    private String id;

    public ResourceClassException(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }
}
