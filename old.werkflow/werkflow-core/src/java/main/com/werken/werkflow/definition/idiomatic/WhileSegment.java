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
    extends ConditionalSegment
{
    public WhileSegment(Expression condition,
                        Segment segment)
    {
        super( condition,
               segment );
    }

    public DefaultTransition[] build(NetBuilder builder)
        throws PetriException
    {
        DefaultTransition[] ends  = super.build( builder );

        DefaultTransition firstTransition = builder.newTransition();
        DefaultPlace      in              = builder.newPlace();

        builder.connect( firstTransition,
                         in );

        DefaultPlace      out            = builder.newPlace();
        DefaultTransition lastTransition = builder.newTransition();

        builder.connect( out,
                         lastTransition );

        builder.connect( in,
                         ends[0] );

        builder.connect( ends[1],
                         in );

        DefaultTransition falseTransition = builder.newTransition();

        builder.connect( in,
                         falseTransition );

        builder.connect( falseTransition,
                         out );

        return new DefaultTransition[]
            {
                firstTransition,
                lastTransition
            };
    }
}
