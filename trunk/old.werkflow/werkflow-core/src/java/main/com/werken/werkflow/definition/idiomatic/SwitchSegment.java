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

    public DefaultTransition[] build(NetBuilder builder)
        throws PetriException
    {

        DefaultTransition firstTransition = builder.newTransition();
        DefaultTransition lastTransition  = builder.newTransition();

        DefaultPlace in = builder.newPlace();

        builder.connect( firstTransition,
                         in );

        DefaultPlace out = builder.newPlace();

        builder.connect( out,
                         lastTransition );

        Iterator segmentIter = this.segments.iterator();
        Segment  eachSegment = null;

        while ( segmentIter.hasNext() )
        {
            eachSegment = (Segment) segmentIter.next();

            DefaultTransition[] branchEnds = eachSegment.build( builder );

            builder.connect( in,
                             branchEnds[0] );

            builder.connect( branchEnds[1],
                             out );
        }

        return new DefaultTransition[]
            {
                firstTransition,
                lastTransition
            };
    }
}
