package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.DefaultPlace;
import com.werken.werkflow.definition.petri.PetriException;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class SequenceSegment
    extends ComplexSegment
{
    private List segments;

    public SequenceSegment()
    {
        this.segments = new ArrayList();
    }

    public void addSegment(Segment segment)
        throws UnsupportedIdiomException
    {
        this.segments.add( segment );
    }

    public DefaultPlace append(DefaultPlace in,
                               NetBuilder builder)
        throws PetriException
    {
        DefaultPlace cur = in;

        Iterator segmentIter = this.segments.iterator();
        Segment  eachSegment = null;

        while ( segmentIter.hasNext() )
        {
            eachSegment = (Segment) segmentIter.next();

            cur = eachSegment.append( cur,
                                      builder );
        }

        return cur;
    }
}
