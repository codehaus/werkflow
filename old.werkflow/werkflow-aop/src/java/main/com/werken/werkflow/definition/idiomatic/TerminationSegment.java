package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.Expression;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.DefaultPlace;
import com.werken.werkflow.definition.petri.DefaultArc;
import com.werken.werkflow.definition.petri.PetriException;

import com.werken.werkflow.action.Action;
import com.werken.werkflow.task.DefaultTask;

public class TerminationSegment
    extends AtomicSegment
{
    public TerminationSegment()
    {
    }

    public DefaultTransition[] build(NetBuilder builder)
        throws PetriException
    {
        DefaultTransition firstTransition = builder.newTransition();
        DefaultTransition lastTransition  = builder.newTransition();

        builder.connect( firstTransition,
                         builder.getOut() );

        DefaultPlace connectingPlace = builder.newPlace();

        DefaultArc falseArc = builder.connect( firstTransition,
                                               connectingPlace );

        falseArc.setExpression( Expression.FALSE );

        builder.connect( connectingPlace,
                         lastTransition );

        return new DefaultTransition[]
            {
                firstTransition,
                lastTransition
            };
    }
}
