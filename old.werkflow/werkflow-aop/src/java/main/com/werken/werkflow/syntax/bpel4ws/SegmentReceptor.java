package com.werken.werkflow.syntax.bpel4ws;

import com.werken.werkflow.definition.idiomatic.Segment;
import com.werken.werkflow.definition.idiomatic.UnsupportedIdiomException;

public interface SegmentReceptor
{
    void receiveSegment(Segment segment)
        throws UnsupportedIdiomException;
}
