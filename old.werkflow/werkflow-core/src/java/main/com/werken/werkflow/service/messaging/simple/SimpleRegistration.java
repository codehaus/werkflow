package com.werken.werkflow.service.messaging.simple;

import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.service.messaging.MessageSink;
import com.werken.werkflow.service.messaging.Registration;

public class SimpleRegistration
    implements Registration
{
    private SimpleMessagingManager manager;

    private MessageSink sink;
    private MessageType messageType;

    public SimpleRegistration(SimpleMessagingManager manager,
                              MessageSink sink,
                              MessageType messageType)
    {
        this.manager     = manager;
        this.sink        = sink;
        this.messageType = messageType;
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
            getMessageSink().acceptMessage( getMessageType(),
                                            message );
        }
    }

    public void unregister()
    {
        getMessagingManager().unregister( this );
    }
}
