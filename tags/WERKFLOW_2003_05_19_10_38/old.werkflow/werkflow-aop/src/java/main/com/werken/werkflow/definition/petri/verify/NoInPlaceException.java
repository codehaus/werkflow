package com.werken.werkflow.definition.petri.verify;

import com.werken.werkflow.definition.petri.Net;

public class NoInPlaceException
    extends VerificationException
{
    public NoInPlaceException(Net net)
    {
        super( net );
    }
}
