package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.DefaultPlace;
import com.werken.werkflow.definition.petri.PetriException;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class SwitchSegment
    extends ComplexSegment
{
    private List segments;

    public SwitchSegment()
    {
        this.segments = new ArrayList();
    }

    public void addSegment(Segment segment)
    {
        this.segments.add( segment );
    }

    public DefaultPlace append(DefaultPlace in,
                               NetBuilder builder)
        throws PetriException
    {
        DefaultPlace out = builder.newPlace();

        Iterator segmentIter = this.segments.iterator();
        Segment  eachSegment = null;

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
