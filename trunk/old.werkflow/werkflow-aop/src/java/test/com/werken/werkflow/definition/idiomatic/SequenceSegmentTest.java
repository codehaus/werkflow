package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.petri.Net;

public class SequenceSegmentTest
    extends SegmentTestBase
{
    public void testConnectedness()
        throws Exception
    {
        SequenceSegment segment = new SequenceSegment();

        testConnectedness( segment );
    }
}
