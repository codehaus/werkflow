package com.werken.werkflow.service.messaging;

import com.werken.werkflow.definition.MessageType;

public interface Registration
{
    static final Registration[] EMPTY_ARRAY = new Registration[0];

    MessageType getMessageType();

    void unregister();
}
