package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.Expression;
import com.werken.werkflow.definition.petri.DefaultPlace;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.PetriException;

public class ConditionalSegment
    implements Segment
{
    private Expression condition;
    private Segment segment;

    public ConditionalSegment(Expression condition,
                              Segment segment)
    {
        this.condition = condition;

        this.segment = segment;
    }

    public void addSegment(Segment segment)
        throws UnsupportedIdiomException
    {
        this.segment.addSegment( segment );
    }

    public DefaultTransition[] build(NetBuilder builder)
        throws PetriException
    {
        DefaultTransition ends[] = this.segment.build( builder );

        DefaultTransition condTrans = builder.newTransition();

        condTrans.setExpression( this.condition );

        DefaultPlace connectingPlace = builder.newPlace();

        builder.connect( condTrans,
                         connectingPlace );

        builder.connect( connectingPlace,
                         ends[0] );

        return new DefaultTransition[]
            {
                condTrans,
                ends[1]
            };
    }
}
