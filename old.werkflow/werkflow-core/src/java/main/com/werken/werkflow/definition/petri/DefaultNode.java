package com.werken.werkflow.definition.petri;

import java.util.List;
import java.util.ArrayList;

public class DefaultNode
    extends DefaultElement
    implements Node
{
    private String id;

    private List inboundArcs;
    private List outboundArcs;

    public DefaultNode(String id)
    {
        this.id = id;
        this.inboundArcs  = new ArrayList();
        this.outboundArcs = new ArrayList();
    }

    public String getId()
    {
        return this.id;
    }

    void addInboundArc(Arc arc)
    {
        this.inboundArcs.add( arc );
    }

    protected Arc[] getInboundArcs()
    {
        return (Arc[]) this.inboundArcs.toArray( Arc.EMPTY_ARRAY );
    }

    void addOutboundArc(Arc arc)
    {
        this.outboundArcs.add( arc );
    }

    protected Arc[] getOutboundArcs()
    {
        return (Arc[]) this.outboundArcs.toArray( Arc.EMPTY_ARRAY );
    }
}

