package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.Expression;
import com.werken.werkflow.definition.petri.Net;

public class SwitchSegmentTest
    extends SegmentTestBase
{
    public void testConnectedness()
        throws Exception
    {
        SwitchSegment segment = new SwitchSegment();

        segment.addSegment( new EmptySegment() );
        segment.addSegment( new EmptySegment() );
        segment.addSegment( new EmptySegment() );
        segment.addSegment( new EmptySegment() );

        testConnectedness( segment );
    }
}
