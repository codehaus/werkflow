package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.DefaultPlace;
import com.werken.werkflow.definition.petri.PetriException;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ParallelSegment
    extends ComplexSegment
{
    private List segments;

    public ParallelSegment()
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
        DefaultTransition splitTrans = builder.newTransition();

        builder.connect( in,
                         splitTrans );

        DefaultTransition joinTrans  = builder.newTransition();

        Iterator segmentIter = this.segments.iterator();
        Segment  eachSegment = null;

        while ( segmentIter.hasNext() )
        {
            eachSegment = (Segment) segmentIter.next();

            DefaultPlace head = builder.newPlace();

            builder.connect( splitTrans,
                             head );

            DefaultPlace tail = eachSegment.append( head,
                                                    builder );

            builder.connect( tail,
                             joinTrans );
        }

        DefaultPlace out = builder.newPlace();

        builder.connect( joinTrans,
                         out );

        return out;
    }
}
