package com.werken.werkflow.engine;

import com.werken.werkflow.definition.MessageType;

public class Message
{
    private MessageType messageType;
    private Object message;

    public Message(MessageType messageType,
                   Object message)
    {
        this.messageType = messageType;
        this.message     = message;
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
