package com.werken.werkflow.service.messaging;

import com.werken.werkflow.definition.MessageType;

public interface MessagingManager
{
    Registration register(MessageSink sink,
                          MessageType messageType)
        throws IncompatibleMessageSelectorException;
}
