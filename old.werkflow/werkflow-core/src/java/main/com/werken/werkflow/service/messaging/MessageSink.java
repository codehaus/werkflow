package com.werken.werkflow.service.messaging;

public interface MessageSink
{
    void acceptMessage(Message message);
}
