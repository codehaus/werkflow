package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.Expression;
import com.werken.werkflow.definition.petri.Net;

public class WhileSegmentTest
    extends SegmentTestBase
{
    public void testConnectedness()
        throws Exception
    {
        WhileSegment segment = new WhileSegment( Expression.TRUE );

        testConnectedness( segment );
    }
}
