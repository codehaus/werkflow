package com.werken.werkflow.core;

import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.definition.MessageCorrelator;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.NoSuchMessageException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

class MessageWaiterHandler
{
    private Transition transition;
    private Map messages;
    private List processCases;

    private Registration registration;

    MessageWaiterHandler(Registration registration,
                         Transition transition)
    {
        this.registration = registration;
        this.transition   = transition;
        this.messages     = new HashMap();
        this.processCases = new ArrayList();
    }

    Registration getRegistration()
    {
        return this.registration;
    }

    Transition getTransition()
    {
        return this.transition;
    }

    boolean acceptMessage(Message message)
    {
        System.err.println( "MessageWaiterHandler.acceptMessage( " + message.getMessage() + " )" );
        boolean result = false;

        this.messages.put( message.getId(),
                           message );

        result = attemptCorrelation( message );


        System.err.println( "RESULT: " +  result );
        return result;
    }

    boolean addCase(CoreProcessCase processCase)
    {
        System.err.println( "MessageWaiterHandler.CASECASECASE( " + processCase.getId() + " )" );

        if ( this.processCases.contains( processCase ) )
        {
            return false;
        }

        this.processCases.add( processCase );

        return attemptCorrelation( processCase );
    }

    void removeCase(CoreProcessCase processCase)
    {
        processCase.removeCorrelationsByTransition( transition.getId() );
        this.processCases.remove( processCase );
    }

    boolean attemptCorrelation(CoreProcessCase processCase)
    {
        boolean result = false;

        Iterator messageIter = this.messages.values().iterator();
        Message  eachMessage = null;

        while ( messageIter.hasNext() )
        {
            eachMessage = (Message) messageIter.next();

            result = ( attemptCorrelation( processCase,
                                           eachMessage )
                       ||
                       result );
        }

        return result;
    }

    boolean attemptCorrelation(Message message)
    {
        boolean result = false;

        Iterator        caseIter = this.processCases.iterator();
        CoreProcessCase eachCase = null;

        while ( caseIter.hasNext() )
        {
            eachCase = (CoreProcessCase) caseIter.next();

            result = ( attemptCorrelation( eachCase,
                                           message )
                       ||
                       result );
        }

        return result;
    }

    boolean attemptCorrelation(CoreProcessCase processCase,
                               Message message)
    {
        MessageWaiter waiter = (MessageWaiter) getTransition().getWaiter();

        MessageCorrelator correlator = waiter.getMessageCorrelator();

        System.err.println( "transition: " + getTransition().getId() + " // " + correlator );

        try
        {
            boolean result = correlator.correlates( message.getMessage(),
                                                    processCase );

            System.err.println( "YO RESULT: " + result );

            return result;
        }
        catch (Exception e)
        {
            // INTERNAL ERROR
            e.printStackTrace();
        }

        return false;
    }

    Message consumeMessage(CoreChangeSet changeSet,
                           CoreProcessCase processCase,
                           String messageId)
        throws NoSuchMessageException
    {
        Message message = getRegistration().consumeMessage( messageId );

        this.messages.remove( messageId );
        this.processCases.remove( processCase );

        return message;
    }
}
