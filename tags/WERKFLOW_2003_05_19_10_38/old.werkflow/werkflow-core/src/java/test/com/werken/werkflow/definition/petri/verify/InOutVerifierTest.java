package com.werken.werkflow.definition.petri.verify;

import com.werken.werkflow.definition.petri.DefaultNet;

import junit.framework.TestCase;

public class InOutVerifierTest
    extends TestCase
{
    public void testNoInPlace()
        throws Exception
    {
        DefaultNet net = new DefaultNet();

        net.addPlace( "out" );

        InOutVerifier verifier = new InOutVerifier();

        try
        {
            verifier.verify( net );
            fail( "should have thrown NoInPlaceException" );
        }
        catch (NoInPlaceException e)
        {
            // expected and correct
        }
    }

    public void testNoOutPlace()
        throws Exception
    {
        DefaultNet net = new DefaultNet();

        net.addPlace( "in" );

        InOutVerifier verifier = new InOutVerifier();

        try
        {
            verifier.verify( net );
            fail( "should have thrown NoInPlaceException" );
        }
        catch (NoOutPlaceException e)
        {
            // expected and correct
        }
    }
}
