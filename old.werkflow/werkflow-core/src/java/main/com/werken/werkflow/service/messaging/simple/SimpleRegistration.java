package com.werken.werkflow.service.messaging.simple;

import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.DefaultMessage;
import com.werken.werkflow.service.messaging.MessageSink;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.NoSuchMessageException;

import java.util.Map;
import java.util.HashMap;

public class SimpleRegistration
    implements Registration
{
    private SimpleMessagingManager manager;

    private long idCounter;

    private MessageSink sink;
    private MessageType messageType;

    private Map messages;

    public SimpleRegistration(SimpleMessagingManager manager,
                              MessageSink sink,
                              MessageType messageType)
    {
        this.manager     = manager;
        this.sink        = sink;
        this.messageType = messageType;
        this.messages    = new HashMap();
    }

    public SimpleMessagingManager getMessagingManager()
    {
        return this.manager;
    }

    public MessageSink getMessageSink()
    {
        return this.sink;
    }

    public MessageType getMessageType()
    {
        return this.messageType;
    }

    public void acceptMessage(Object message)
    {
        if ( ((SimpleMessageSelector)getMessageType().getMessageSelector()).selects( message ) )
        {
            String msgId = "msg." + (++this.idCounter);

            Message messageWrapper = new DefaultMessage( msgId,
                                                         getMessageType(),
                                                         message );

            this.messages.put( msgId,
                               messageWrapper );

            getMessageSink().acceptMessage( messageWrapper );
        }
    }

    public void unregister()
    {
        getMessagingManager().unregister( this );
    }

    public Message getMessage(String id)
        throws NoSuchMessageException
    {
        Message message = (Message) this.messages.get( id );

        if ( message == null )
        {
            throw new NoSuchMessageException( id );
        }

        return message;
    }

    public void consumeMessage(String id)
        throws NoSuchMessageException
    {
        if ( ! this.messages.containsKey( id ) )
        {
            throw new NoSuchMessageException( id );
        }
        
        this.messages.remove( id );
    }
}
