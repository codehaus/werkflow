package com.werken.werkflow.definition.petri;

public class NoSuchParameterException
    extends IdiomException
{
    private String id;

    public NoSuchParameterException(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }
}
