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

    public DefaultTransition[] build(NetBuilder builder)
        throws PetriException
    {
        Iterator segmentIter = this.segments.iterator();
        Segment  eachSegment = null;

        DefaultTransition firstTransition = builder.newTransition();
        DefaultTransition lastTransition = builder.newTransition();

        while ( segmentIter.hasNext() )
        {
            eachSegment = (Segment) segmentIter.next();

            DefaultTransition[] ends = eachSegment.build( builder );

            DefaultPlace in  = builder.newPlace();
            DefaultPlace out = builder.newPlace();

            builder.connect( in,
                             ends[0] );

            builder.connect( ends[1],
                             out );

            builder.connect( firstTransition,
                             in );

            builder.connect( out,
                             lastTransition );
        }

        return new DefaultTransition[]
            {
                firstTransition,
                lastTransition
            };
    }
}
