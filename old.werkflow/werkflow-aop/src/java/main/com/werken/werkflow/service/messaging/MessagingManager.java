package com.werken.werkflow.service.messaging;

public interface MessagingManager
{
    Registration register(MessageSink sink,
                          MessageSelector selector)
        throws IncompatibleMessageSelectorException;
}
