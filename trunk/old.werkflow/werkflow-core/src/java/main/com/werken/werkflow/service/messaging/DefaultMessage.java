package com.werken.werkflow.service.messaging;

import com.werken.werkflow.definition.MessageType;

public class DefaultMessage
    implements Message
{
    private String id;
    private MessageType messageType;
    private Object message;

    public DefaultMessage(String id,
                          MessageType messageType,
                          Object message)
    {
        this.id          = id;
        this.messageType = messageType;
        this.message     = message;
    }

    public String getId()
    {
        return this.id;
    }

    public MessageType getMessageType()
    {
        return this.messageType;
    }

    public Object getMessage()
    {
        return this.message;
    }
}

