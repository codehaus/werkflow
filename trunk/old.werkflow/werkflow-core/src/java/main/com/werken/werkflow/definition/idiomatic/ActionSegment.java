package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.PetriException;

import com.werken.werkflow.action.Action;
import com.werken.werkflow.task.DefaultTask;

public class ActionSegment
    extends AtomicSegment
{
    private Action action;

    public ActionSegment(Action action)
    {
        this.action = action;
    }

    public Action getAction()
    {
        return this.action;
    }

    public DefaultTransition[] build(NetBuilder builder)
        throws PetriException
    {
        DefaultTransition transition = builder.newTransition();

        DefaultTask task = new DefaultTask();

        task.setAction( getAction() );

        transition.setTask( task );

        return new DefaultTransition[]
            {
                transition,
                transition
            };
    }
}
