package com.werken.werkflow.service.messaging;

import com.werken.werkflow.definition.MessageType;

public class MockMessageSink
    implements MessageSink
{
    private Message message;

    public MockMessageSink()
    {

    }

    public void acceptMessage(Message message)
    {
        this.message     = message;
    }

    public Message getMessage()
    {
        return this.message;
    }
}
