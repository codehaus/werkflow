package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.Expression;
import com.werken.werkflow.definition.petri.DefaultPlace;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.PetriException;

public class ConditionalSegment
    implements Segment
{
    private Expression condition;
    private Segment bodySegment;

    public ConditionalSegment(Expression condition,
                              Segment bodySegment)
    {
        this.condition   = condition;
        this.bodySegment = bodySegment;
    }

    public ConditionalSegment(Expression condition)
    {
        this.condition   = condition;
        this.bodySegment = new SequenceSegment();
    }

    protected Segment getBodySegment()
    {
        return this.bodySegment;
    }

    public void addSegment(Segment segment)
        throws UnsupportedIdiomException
    {
        this.bodySegment.addSegment( segment );
    }

    public DefaultPlace append(DefaultPlace in,
                               NetBuilder builder)
        throws PetriException
    {
        DefaultTransition condTrans = builder.newTransition();

        condTrans.setExpression( this.condition );

        builder.connect( in,
                         condTrans );

        DefaultPlace connectingPlace = builder.newPlace();

        builder.connect( condTrans,
                         connectingPlace );
        
        return this.bodySegment.append( connectingPlace,
                                        builder );
    }
}
