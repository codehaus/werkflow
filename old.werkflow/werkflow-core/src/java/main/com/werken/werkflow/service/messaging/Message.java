package com.werken.werkflow.service.messaging;

import com.werken.werkflow.definition.MessageType;

public interface Message
{
    String getId();
    MessageType getMessageType();
    Object getMessage();
}
