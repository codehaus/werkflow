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

    public DefaultPlace append(DefaultPlace in,
                               NetBuilder builder)
        throws PetriException
    {
        DefaultPlace out = builder.getOut();

        builder.connect( in,
                         out );

        DefaultPlace falseOut = builder.newPlace();
        DefaultTransition falseTrans = builder.newTransition();

        falseTrans.setExpression( Expression.FALSE );

        builder.connect( in,
                         falseTrans );

        builder.connect( falseTrans,
                         falseOut );

        return falseOut;
    }
}
