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
    public WhileSegment(Expression condition)
    {
        super( condition );
    }

    public DefaultPlace append(DefaultPlace in,
                               NetBuilder builder)
        throws PetriException
    {
        DefaultPlace tail = super.append( in,
                                          builder );


        builder.connect( tail,
                         in );

        DefaultPlace out = builder.newPlace();

        builder.connect( in,
                         out );
        

        return out;
    }
}
