package com.werken.werkflow.definition.petri;

public class NoSuchNodeException
    extends NetException
{
    private String id;

    public NoSuchNodeException(Net net,
                               String id)
    {
        super( net );

        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }
}
