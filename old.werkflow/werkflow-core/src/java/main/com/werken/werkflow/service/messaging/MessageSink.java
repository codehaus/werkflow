package com.werken.werkflow.service.messaging;

import com.werken.werkflow.definition.MessageType;

public interface MessageSink
{
    void acceptMessage(MessageType messageType,
                       Object message);
}
