package com.werken.werkflow;

public class NoSuchProcessException
    extends WerkflowException
{
    private String id;

    public NoSuchProcessException(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }
} 
