package com.werken.werkflow.definition.petri;

public class NoSuchTransitionException
    extends NoSuchNodeException
{
    public NoSuchTransitionException(Net net,
                                     String id)
    {
        super( net,
               id );
    }
}
