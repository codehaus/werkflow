package com.werken.werkflow.definition.petri.verify;

import com.werken.werkflow.definition.petri.Net;

public class NoOutPlaceException
    extends VerificationException
{
    public NoOutPlaceException(Net net)
    {
        super( net );
    }
}
