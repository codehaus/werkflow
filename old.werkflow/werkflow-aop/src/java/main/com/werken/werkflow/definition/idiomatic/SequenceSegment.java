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

    public DefaultTransition[] build(NetBuilder builder)
        throws PetriException
    {
        if ( this.segments.isEmpty() )
        {
            DefaultTransition noOp = builder.newTransition();

            return new DefaultTransition[]
                {
                    noOp,
                    noOp
                };
        }

        Iterator segmentIter = this.segments.iterator();
        Segment  eachSegment = null;

        DefaultTransition firstTransition = null;
        DefaultTransition lastTransition  = null;

        DefaultTransition currentTransition = null;

        while ( segmentIter.hasNext() )
        {
            eachSegment = (Segment) segmentIter.next();

            DefaultTransition[] ends = eachSegment.build( builder );

            if ( currentTransition != null )
            {
                DefaultPlace connectingPlace = builder.newPlace();

                builder.connect( currentTransition,
                                 connectingPlace );

                builder.connect( connectingPlace,
                                 ends[0] );
            }

            if ( firstTransition == null )
            {
                firstTransition = ends[0];
            }

            if ( ! segmentIter.hasNext() )
            {
                lastTransition = ends[1];
            }
            else
            {
                currentTransition = ends[1];
            }
        }

        return new DefaultTransition[]
            {
                firstTransition,
                lastTransition
            };
    }
}
