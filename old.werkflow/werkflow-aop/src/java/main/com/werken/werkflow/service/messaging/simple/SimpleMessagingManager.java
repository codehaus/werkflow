package com.werken.werkflow.service.messaging.simple;

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

import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.service.messaging.MessagingManager;
import com.werken.werkflow.service.messaging.MessageSink;
import com.werken.werkflow.service.messaging.MessageSelector;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.IncompatibleMessageSelectorException;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

/** Simple <code>MessagingManager</code> implementation.
 *
 *  <p>
 *  The <code>SimpleMessagingManager</code> is purely a convenience
 *  implementation and only works with subclasses of <code>SimpleSelector</code>s.
 *  It manages the details of the {@link MessagingManager#register} method allowing
 *  client code to simply feed all inbound messages via the single-point of entry
 *  of {@link #acceptMessage}.
 *  </p>
 *
 *  @see #acceptMessage
 *  @see SimpleMessageSelector
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class SimpleMessagingManager
    implements MessagingManager
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Registrations. */
    private List registrations;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public SimpleMessagingManager()
    {
        this.registrations = new LinkedList();
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** @see MessagingManager
     */
    public Registration register(MessageSink sink,
                                 MessageType messageType)
        throws IncompatibleMessageSelectorException
    {
        MessageSelector selector = messageType.getMessageSelector();

        if ( ! ( selector instanceof SimpleMessageSelector ) )
        {
            throw new IncompatibleMessageSelectorException( selector );
        }

        SimpleRegistration registration = new SimpleRegistration( this,
                                                                  sink,
                                                                  messageType );

        this.registrations.add( registration );

        return registration;
    }

    /** Unregister a <code>SimpleRegistration</code>.
     *
     *  @param registration The registration to unregister.
     */
    public void unregister(SimpleRegistration registration)
    {
        while ( this.registrations.remove( registration ) )
        {
            // intentionally left blank
        }
    }

    /** Accept a message for delegation.
     *
     *  @param message The message.
     */
    public void acceptMessage(Object message)
        throws Exception
    {
        Iterator           regIter = this.registrations.iterator();
        SimpleRegistration eachReg = null;

        while ( regIter.hasNext() )
        {
            eachReg = (SimpleRegistration) regIter.next();

            eachReg.acceptMessage( message );
        }
    }
}
