package com.werken.werkflow.definition.petri.verify;

import com.werken.werkflow.definition.petri.Net;

public interface Verifier
{
    void verify(Net net)
        throws VerificationException;
}
