package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.petri.Net;

public class EmptySegmentTest
    extends SegmentTestBase
{
    public void testConnectedness()
        throws Exception
    {
        EmptySegment segment = new EmptySegment();

        testConnectedness( segment );
    }
}
