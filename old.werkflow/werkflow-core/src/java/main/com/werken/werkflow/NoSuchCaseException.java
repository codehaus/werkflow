package com.werken.werkflow;

public class NoSuchCaseException
    extends WerkflowException
{
    private String id;

    public NoSuchCaseException(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }
} 
