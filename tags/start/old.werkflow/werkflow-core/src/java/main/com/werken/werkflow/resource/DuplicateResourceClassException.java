package com.werken.werkflow.resource;

public class DuplicateResourceClassException
    extends ResourceClassException
{
    public DuplicateResourceClassException(String id)
    {
        super( id );
    }
}
