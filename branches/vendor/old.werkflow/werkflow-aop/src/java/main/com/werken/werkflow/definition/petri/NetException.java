package com.werken.werkflow.definition.petri;

public class NetException
    extends PetriException
{
    private Net net;

    public NetException(Net net)
    {
        this.net = net;
    }

    public Net getNet()
    {
        return this.net;
    }
}
