package com.werken.werkflow.core;

import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.NoSuchMessageException;

interface TerminalMessageHandler
{
    boolean acceptMessage(CoreChangeSet changeSet,
                          Message message);

    Message consumeMessage(CoreChangeSet changeSet,
                           CoreProcessCase processCase,
                           String transitionId,
                           String messageId)
        throws NoSuchMessageException;

    void add(Transition transition);
}
