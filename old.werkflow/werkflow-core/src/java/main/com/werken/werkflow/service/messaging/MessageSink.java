package com.werken.werkflow.service.messaging;

public interface MessageSink
{
    void acceptMessage(MessageSelector selector,
                       Object message);
}
