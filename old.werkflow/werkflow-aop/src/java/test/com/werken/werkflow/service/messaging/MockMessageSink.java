package com.werken.werkflow.service.messaging;

import com.werken.werkflow.definition.MessageType;

public class MockMessageSink
    implements MessageSink
{
    private MessageType messageType;
    private Object message;

    public MockMessageSink()
    {

    }

    public void acceptMessage(MessageType messageType,
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
