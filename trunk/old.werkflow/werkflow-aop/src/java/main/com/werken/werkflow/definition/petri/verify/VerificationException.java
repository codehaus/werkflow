package com.werken.werkflow.definition.petri.verify;

import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.PetriException;

public class VerificationException
    extends PetriException
{
    public static final VerificationException[] EMPTY_ARRAY = new VerificationException[0];

    private Net net;

    public VerificationException(Net net)
    {
        this.net = net;
    }

    public Net getNet()
    {
        return this.net;
    }
}
