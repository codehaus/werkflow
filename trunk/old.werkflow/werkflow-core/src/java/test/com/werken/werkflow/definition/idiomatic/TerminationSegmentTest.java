package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.petri.Net;

public class TerminationSegmentTest
    extends SegmentTestBase
{
    public void testConnectedness()
        throws Exception
    {
        TerminationSegment segment = new TerminationSegment();

        testConnectedness( segment );
    }
}
