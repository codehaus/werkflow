package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.DefaultPlace;
import com.werken.werkflow.definition.petri.PetriException;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class PickSegment
    extends ComplexSegment
{
    private List segments;

    public PickSegment()
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
        Iterator segmentIter = this.segments.iterator();
        Segment  eachSegment = null;

        DefaultPlace out = builder.newPlace();

        while ( segmentIter.hasNext() )
        {
            eachSegment = (Segment) segmentIter.next();

            DefaultPlace tail = eachSegment.append( in,
                                                    builder );

            builder.connect( tail,
                             out );
        }

        return out;
    }
}
