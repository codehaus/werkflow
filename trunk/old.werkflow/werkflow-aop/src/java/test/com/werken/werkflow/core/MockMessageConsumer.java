package com.werken.werkflow.core;

import com.werken.werkflow.definition.petri.Transition;

import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.NoSuchMessageException;

import java.util.Map;
import java.util.HashMap;

class MockMessageConsumer
    implements MessageConsumer
{
    private Map messages;

    MockMessageConsumer()
    {
        this.messages = new HashMap();
    }

    void addMessage(Message message)
    {
        this.messages.put( message.getId(),
                           message );
    }

    public Message consumeMessage(CoreChangeSet changeSet,
                                  CoreProcessCase processCase,
                                  String transitionId,
                                  String messageId)
        throws NoSuchMessageException
    {
        Message message = (Message) this.messages.get( messageId );

        if ( message == null )
        {
            throw new NoSuchMessageException( messageId );
        }

        this.messages.remove( messageId );

        return message;
    }
}
