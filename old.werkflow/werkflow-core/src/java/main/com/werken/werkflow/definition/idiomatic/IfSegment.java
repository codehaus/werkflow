package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.Expression;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.DefaultPlace;
import com.werken.werkflow.definition.petri.DefaultArc;
import com.werken.werkflow.definition.petri.PetriException;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class IfSegment
    extends ConditionalSegment
{
    public IfSegment(Expression condition,
                     Segment segment)
    {
        super( condition,
               segment );
    }

    public DefaultPlace build(DefaultPlace in,
                              NetBuilder builder)
        throws PetriException
    {
        DefaultPlace out = super.append( in,
                                         builder );

        builder.connect( in,
                         out );

        return out;
    }
}
