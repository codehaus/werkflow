package com.werken.werkflow.definition.petri;

public class DuplicateIdException
    extends NetException
{
    private String id;

    public DuplicateIdException(Net net,
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
