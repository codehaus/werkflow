package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.Expression;
import com.werken.werkflow.definition.petri.Net;

public class PickSegmentTest
    extends SegmentTestBase
{
    public void testConnectedness()
        throws Exception
    {
        PickSegment segment = new PickSegment();

        segment.addSegment( new EmptySegment() );
        segment.addSegment( new EmptySegment() );
        segment.addSegment( new EmptySegment() );
        segment.addSegment( new EmptySegment() );

        testConnectedness( segment );
    }
}
