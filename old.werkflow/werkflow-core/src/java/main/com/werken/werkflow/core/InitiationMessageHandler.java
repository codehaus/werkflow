package com.werken.werkflow.core;

import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.NoSuchMessageException;

class InitiationMessageHandler
    implements TerminalMessageHandler
{
    public boolean acceptMessage(CoreChangeSet changeSet,
                                 Message message)
    {
        return true;
    }

    public Message consumeMessage(CoreChangeSet changeSet,
                                  CoreProcessCase processCase,
                                  String transitionId,
                                  String messageId)
        throws NoSuchMessageException
    {
        return null;
    }

    public void add(Transition transition)
    {

    }

    public boolean addCase(CoreChangeSet changeSet,
                           CoreProcessCase processCase,
                           String transitionId)
    {
        return false;
    }
}
