package org.codehaus.werkflow.core;

/*
 $Id$

 Copyright 2003 (C) The Codehaus. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "werkflow" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Codehaus.  For written permission,
    please contact info@codehaus.org.

 4. Products derived from this Software may not be called "werkflow"
    nor may "werkflow" appear in their names without prior written
    permission of The Codehaus. "werkflow" is a registered
    trademark of The Codehaus.

 5. Due credit should be given to The Codehaus -
    http://werkflow.codehaus.org/

 THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import org.codehaus.werkflow.definition.MessageCorrelator;
import org.codehaus.werkflow.definition.MessageWaiter;
import org.codehaus.werkflow.definition.petri.Transition;
import org.codehaus.werkflow.service.messaging.Message;
import org.codehaus.werkflow.service.messaging.Registration;
import org.codehaus.werkflow.service.messaging.NoSuchMessageException;

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
        boolean result = false;

        this.messages.put( message.getId(),
                           message );

        result = attemptCorrelation( message );

        return result;
    }

    boolean addCase(CoreProcessCase processCase)
    {
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

        if ( correlator == null )
        {
            return true;
        }

        try
        {
            boolean result = correlator.correlates( message.getMessage(),
                                                    processCase );

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
