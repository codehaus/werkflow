package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.PetriException;

import com.werken.werkflow.action.Action;
import com.werken.werkflow.task.DefaultTask;

public class EmptySegment
    extends AtomicSegment
{
    public EmptySegment()
    {
    }

    public DefaultTransition[] build(NetBuilder builder)
        throws PetriException
    {
        DefaultTransition transition = builder.newTransition();

        return new DefaultTransition[]
            {
                transition,
                transition
            };
    }
}
