package com.werken.werkflow.definition.petri;

public class DefaultPlace
    extends DefaultNode
    implements Place
{
    public DefaultPlace(String id)
    {
        super( id );
    }

    public Arc[] getArcsToTransitions()
    {
        return getOutboundArcs();
    }

    public Arc[] getArcsFromTransitions()
    {
        return getInboundArcs();
    }
}
