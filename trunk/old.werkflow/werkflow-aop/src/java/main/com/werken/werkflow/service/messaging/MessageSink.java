package com.werken.werkflow.service.messaging;

public interface MessageSink
{
    void acceptMessage(MessageInterest interest,
                       Object message);
}
