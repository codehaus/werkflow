package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.Expression;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.DefaultPlace;
import com.werken.werkflow.definition.petri.DefaultArc;
import com.werken.werkflow.definition.petri.PetriException;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class WhileSegment
    extends SequenceSegment
{
    private Expression expr;

    public WhileSegment(Expression expr)
    {
        this.expr = expr;
    }

    public Expression getExpression()
    {
        return this.expr;
    }

    public DefaultTransition[] build(NetBuilder builder)
        throws PetriException
    {
        DefaultTransition[] ends  = super.build( builder );

        DefaultTransition firstTransition = builder.newTransition();
        DefaultPlace      inPlace         = builder.newPlace();

        builder.connect( firstTransition,
                         inPlace );

        DefaultPlace      outPlace       = builder.newPlace();
        DefaultTransition lastTransition = builder.newTransition();

        builder.connect( outPlace,
                         lastTransition );

        DefaultTransition trueTransition = builder.newTransition();

        builder.connect( inPlace,
                         trueTransition );

        trueTransition.setExpression( getExpression() );

        DefaultPlace connectingPlace = builder.newPlace();

        builder.connect( trueTransition,
                         connectingPlace );

        builder.connect( connectingPlace,
                         ends[0] );

        builder.connect( ends[1],
                         outPlace );

        DefaultTransition falseTransition = builder.newTransition();

        builder.connect( inPlace,
                         falseTransition );

        builder.connect( falseTransition,
                         outPlace );

        return new DefaultTransition[]
            {
                firstTransition,
                lastTransition
            };
    }
}
