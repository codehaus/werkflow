package com.werken.werkflow.core;

import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.NoSuchMessageException;

class MessageTypeHandler
{
    private TerminalMessageHandler terminalMessageHandler;

    private Registration registration;

    MessageTypeHandler(Registration registration)
    {
        this.registration = registration;
    }

    private Registration getRegistration()
    {
        return this.registration;
    }

    void add(Transition transition)
    {
        if ( this.terminalMessageHandler == null )
        {
            this.terminalMessageHandler = new CorrelationMessageHandler( getRegistration() );
        }

        this.terminalMessageHandler.add( transition );
    }

    private TerminalMessageHandler getTerminalMessageHandler()
    {
        return this.terminalMessageHandler;
    }

    boolean acceptMessage(CoreChangeSet changeSet,
                          Message message)
    {
        System.err.println( "MessageTypeHandler.acceptMessage( " + message.getMessage() + " )" );
        return getTerminalMessageHandler().acceptMessage( changeSet,
                                                          message );
    }

    boolean addCase(CoreChangeSet changeSet,
                    CoreProcessCase processCase,
                    String transitionId)
    {
        return getTerminalMessageHandler().addCase( changeSet,
                                                    processCase,
                                                    transitionId );
    }

    Message consumeMessage(CoreChangeSet changeSet,
                           CoreProcessCase processCase,
                           String transitionId,
                           String messageId)
        throws NoSuchMessageException
    {
        return getTerminalMessageHandler().consumeMessage( changeSet,
                                                           processCase,
                                                           transitionId,
                                                           messageId );
                                                           
    }
}
