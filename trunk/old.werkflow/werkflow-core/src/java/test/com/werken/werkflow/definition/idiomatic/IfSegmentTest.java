package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.Expression;
import com.werken.werkflow.definition.petri.Net;

public class IfSegmentTest
    extends SegmentTestBase
{
    public void testConnectedness()
        throws Exception
    {
        IfSegment segment = new IfSegment( Expression.TRUE,
                                           new EmptySegment() );

        testConnectedness( segment );
    }
}
