package com.werken.werkflow.definition.petri;

public class NoSuchPlaceException
    extends NoSuchNodeException
{
    public NoSuchPlaceException(Net net,
                                String id)
    {
        super( net,
               id );
    }
}
