package com.werken.werkflow.core;

import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.NoSuchMessageException;

interface MessageConsumer
{
    Message consumeMessage(CoreChangeSet changeSet,
                           CoreProcessCase processCase,
                           String transitionId,
                           String messageId)
        throws NoSuchMessageException;
}
