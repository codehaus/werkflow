package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.DefaultPlace;
import com.werken.werkflow.definition.petri.PetriException;

import com.werken.werkflow.action.Action;
import com.werken.werkflow.task.DefaultTask;

public class MessageConditionalSegment
    extends AtomicSegment
{
    private MessageWaiter messageWaiter;
    private Segment bodySegment;

    public MessageConditionalSegment(MessageWaiter messageWaiter,
                                     Segment bodySegment)
    {
        this.messageWaiter = messageWaiter;
        this.bodySegment = bodySegment;
    }

    public DefaultTransition[] build(NetBuilder builder)
        throws PetriException
    {
        DefaultTransition firstTransition = builder.newTransition();

        firstTransition.setMessageWaiter( this.messageWaiter );

        DefaultPlace connectingPlace = builder.newPlace();

        DefaultTransition ends[] = this.bodySegment.build( builder );

        builder.connect( firstTransition,
                         connectingPlace );

        builder.connect( connectingPlace,
                         ends[0] );

        return new DefaultTransition[]
            {
                firstTransition,
                ends[1]
            };
    }
}
