package com.werken.werkflow.definition.idiomatic;

public abstract class AtomicSegment
    implements Segment
{
    public AtomicSegment()
    {

    }

    public void addSegment(Segment segment)
        throws UnsupportedIdiomException
    {
        throw new UnsupportedIdiomException();
    }
}
