package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.idiomatic.Segment;

import org.apache.commons.jelly.JellyTagException;

public interface SegmentReceptor
{
    void receiveSegment(Segment segment)
        throws JellyTagException;
}
