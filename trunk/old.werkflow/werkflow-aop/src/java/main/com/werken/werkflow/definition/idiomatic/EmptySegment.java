package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.DefaultPlace;
import com.werken.werkflow.definition.petri.PetriException;

import com.werken.werkflow.action.Action;
import com.werken.werkflow.task.DefaultTask;

public class EmptySegment
    extends AtomicSegment
{
    public EmptySegment()
    {
    }

    public DefaultPlace append(DefaultPlace in,
                               NetBuilder builder)
        throws PetriException
    {
        return in;
    }
}
