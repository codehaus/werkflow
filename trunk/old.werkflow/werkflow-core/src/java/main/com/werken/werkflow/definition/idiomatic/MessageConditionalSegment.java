package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.DefaultPlace;
import com.werken.werkflow.definition.petri.PetriException;

import com.werken.werkflow.action.Action;
import com.werken.werkflow.task.DefaultTask;

public class MessageConditionalSegment
    extends ComplexSegment
{
    private MessageWaiter messageWaiter;
    private Segment bodySegment;

    public MessageConditionalSegment(MessageWaiter messageWaiter)
    {
        this.messageWaiter = messageWaiter;
        this.bodySegment = bodySegment;
    }


    public void addSegment(Segment segment)
        throws UnsupportedIdiomException
    {
        if ( segment instanceof AtomicSegment )
        {
            this.bodySegment = segment;
        }
        else
        {
            throw new UnsupportedIdiomException();
        }
    }

    public DefaultPlace append(DefaultPlace in,
                               NetBuilder builder)
        throws PetriException
    {
        DefaultTransition msgTrans = builder.newTransition();
        msgTrans.setWaiter( this.messageWaiter );

        builder.connect( in,
                         msgTrans );

        DefaultPlace msgPlace = builder.newPlace();

        builder.connect( msgTrans,
                         msgPlace );

        return this.bodySegment.append( msgPlace,
                                        builder );
    }
}
