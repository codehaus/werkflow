package com.werken.werkflow.core;

import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.NoSuchMessageException;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

class CorrelationMessageHandler
    implements TerminalMessageHandler
{
    private Registration registration;
    private Map messageWaiterHandlers;

    CorrelationMessageHandler(Registration registration)
    {
        this.registration          = registration;
        this.messageWaiterHandlers = new HashMap();
    }

    Registration getRegistration()
    {
        return this.registration;
    }

    public void add(Transition transition)
    {
        this.messageWaiterHandlers.put( transition.getId(),
                                        new MessageWaiterHandler( getRegistration(),
                                                                  transition ) );
    }

    public boolean acceptMessage(CoreChangeSet changeSet,
                                 Message message)
    {
        boolean result = false;

        Iterator waiterIter = this.messageWaiterHandlers.values().iterator();
        MessageWaiterHandler eachWaiter = null;

        while ( waiterIter.hasNext() )
        {
            eachWaiter = (MessageWaiterHandler) waiterIter.next();

            result = ( eachWaiter.acceptMessage( changeSet,
                                                 message )
                       ||
                       result );
        }

        if ( ! result )
        {
            changeSet.addUncorrelated( message );
        }

        return result;
    }

    public Message consumeMessage(CoreChangeSet changeSet,
                                  CoreProcessCase processCase,
                                  String transitionId,
                                  String messageId)
        throws NoSuchMessageException
    {
        MessageWaiterHandler handler = getMessageWaiterHandler( transitionId );

        return handler.consumeMessage( changeSet,
                                       processCase,
                                       messageId );
    }

    MessageWaiterHandler getMessageWaiterHandler(String transitionId)
    {
        return (MessageWaiterHandler) this.messageWaiterHandlers.get( transitionId );
    }
        
}
