package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.action.Action;
import com.werken.werkflow.definition.petri.Net;

public class ActionSegmentTest
    extends SegmentTestBase
{
    public void testConnectedness()
        throws Exception
    {
        ActionSegment segment = new ActionSegment( Action.NOOP );

        testConnectedness( segment );
    }
}
