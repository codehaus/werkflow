package com.werken.werkflow.definition.petri.verify;

import com.werken.werkflow.definition.petri.Net;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class MultiVerifier
    implements Verifier
{
    private List verifiers;

    public MultiVerifier()
    {
        this.verifiers = new ArrayList();
    }

    public void addVerifier(Verifier verifier)
    {
        this.verifiers.add( verifier );
    }

    public void verify(Net net)
        throws VerificationException
    {
        boolean bogus = false;
        MultiVerificationException exception = new MultiVerificationException( net );

        Iterator verifierIter = this.verifiers.iterator();
        Verifier eachVerifier = null;

        while ( verifierIter.hasNext() )
        {
            eachVerifier = (Verifier) verifierIter.next();

            try
            {
                eachVerifier.verify( net );
            }
            catch (VerificationException e)
            {
                exception.addVerificationException( e );
                bogus = true;
            }
        }

        if ( bogus )
        {
            throw exception;
        }
    }
}
