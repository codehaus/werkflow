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

import org.codehaus.werkflow.definition.petri.Transition;
import org.codehaus.werkflow.service.messaging.Message;
import org.codehaus.werkflow.service.messaging.Registration;
import org.codehaus.werkflow.service.messaging.NoSuchMessageException;

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

    void add(Transition transition,
             ProcessDeployment deployment)
    {
        if ( deployment != null )
        {
            this.terminalMessageHandler = new InitiationMessageHandler( getRegistration(),
                                                                        transition,
                                                                        deployment );
        }
        else
        {
            this.terminalMessageHandler = new CorrelationMessageHandler( getRegistration() );
            this.terminalMessageHandler.add( transition );
        }
    }

    private TerminalMessageHandler getTerminalMessageHandler()
    {
        return this.terminalMessageHandler;
    }

    boolean acceptMessage(Message message)
    {
        System.err.println( "MessageTypeHandler.acceptMessage( " + message.getMessage() + " )" );
        return getTerminalMessageHandler().acceptMessage( message );
    }

    boolean addCase(CoreProcessCase processCase,
                    String transitionId)
    {
        return getTerminalMessageHandler().addCase( processCase,
                                                    transitionId );
    }

    void removeCase(CoreProcessCase processCase,
                    String transitionId)
    {
        getTerminalMessageHandler().removeCase( processCase,
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
