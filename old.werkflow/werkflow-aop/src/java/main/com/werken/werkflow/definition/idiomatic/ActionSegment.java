package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.petri.DefaultPlace;
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

    public DefaultPlace append(DefaultPlace in,
                               NetBuilder builder)
        throws PetriException
    {
        DefaultTransition actionTrans = builder.newTransition();

        builder.connect( in,
                         actionTrans );

        DefaultTask task = new DefaultTask();

        task.setAction( getAction() );

        actionTrans.setTask( task );

        DefaultPlace out = builder.newPlace();

        builder.connect( actionTrans,
                         out );

        return out;
    }
}
