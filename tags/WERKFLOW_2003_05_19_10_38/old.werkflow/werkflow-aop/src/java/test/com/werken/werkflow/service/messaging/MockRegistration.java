package com.werken.werkflow.service.messaging;

import com.werken.werkflow.definition.MessageType;

import java.util.Map;
import java.util.HashMap;

public class MockRegistration
    implements Registration
{
    private MessageType messageType;
    private Map messages;
    private boolean unregistered;

    public MockRegistration(MessageType messageType)
    {
        this.messageType = messageType;
        this.messages    = new HashMap();
    }

    public void unregister()
    {
        this.unregistered = true;
    }

    public boolean isUnregistered()
    {
        return this.unregistered;
    }

    public MessageType getMessageType()
    {
        return this.messageType;
    }

    public Message getMessage(String id)
        throws NoSuchMessageException
    {
        if ( this.messages.containsKey( id ) )
        {
            return (Message) this.messages.get( id );
        }

        throw new NoSuchMessageException( id );
    }

    public void addMessage(Message message)
    {
        this.messages.put( message.getId(),
                           message );
    }

    public Message consumeMessage(String id)
        throws NoSuchMessageException
    {
        Message message = getMessage( id );

        this.messages.remove( id );

        return message;
    }
    
}
