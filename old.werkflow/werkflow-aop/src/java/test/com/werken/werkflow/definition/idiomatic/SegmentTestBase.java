package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.verify.ConnectednessVerifier;
import com.werken.werkflow.definition.petri.verify.VerificationException;

import junit.framework.TestCase;

public abstract class SegmentTestBase
    extends TestCase
{
    public void testConnectedness(Segment segment)
        throws Exception
    {
        Net net = buildNet( segment );

        ConnectednessVerifier verifier = new ConnectednessVerifier();

        try
        {
            verifier.verify( net );
        }
        catch (VerificationException e)
        {
            fail( e.getMessage() );
        }
    }

    public Net buildNet(Segment segment)
        throws Exception
    {
        NetBuilder builder = new NetBuilder();

        return builder.build( segment );
    }
}
