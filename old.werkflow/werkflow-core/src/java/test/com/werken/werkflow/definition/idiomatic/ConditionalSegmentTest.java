package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.Expression;
import com.werken.werkflow.definition.petri.Net;

public class ConditionalSegmentTest
    extends SegmentTestBase
{
    public void testConnectedness()
        throws Exception
    {
        ConditionalSegment segment = new ConditionalSegment( Expression.TRUE );

        testConnectedness( segment );
    }
}
