package com.werken.werkflow.resource;

public class NoSuchResourceClassException
    extends ResourceClassException
{
    public NoSuchResourceClassException(String id)
    {
        super( id );
    }
}
