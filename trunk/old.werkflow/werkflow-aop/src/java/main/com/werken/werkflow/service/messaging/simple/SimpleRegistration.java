package com.werken.werkflow.service.messaging.simple;

/*
 $Id$

 Copyright 2003 (C) The Werken Company. All Rights Reserved.
 
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
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.
 
 4. Products derived from this Software may not be called "werkflow"
    nor may "werkflow" appear in their names without prior written
    permission of The Werken Company. "werkflow" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://werkflow.werken.com/).
 
 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.
 
 */

import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.DefaultMessage;
import com.werken.werkflow.service.messaging.MessageSink;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.NoSuchMessageException;

import java.util.Map;
import java.util.HashMap;

/** <code>Registration</code> provided by a <code>SimpleMessagingManager</code>.
 *
 *  @see SimpleMessagingManager
 *  @see MessagingManager#register
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class SimpleRegistration
    implements Registration
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Messaging-manager registered to. */
    private SimpleMessagingManager manager;

    /** Message-id counter. */
    private long idCounter;

    /** Message-sink. */
    private MessageSink sink;

    /** Message-type. */
    private MessageType messageType;

    /** Unconsumed messages. */
    private Map messages;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param manager The constructing messaging-manager.
     *  @param sink The message-sink.
     *  @param messageType The message-type.
     */
    public SimpleRegistration(SimpleMessagingManager manager,
                              MessageSink sink,
                              MessageType messageType)
    {
        this.manager     = manager;
        this.sink        = sink;
        this.messageType = messageType;
        this.messages    = new HashMap();
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Retrieve the <code>MessagingManager</code>.
     *
     *  @return The messaging-manager.
     */
    public SimpleMessagingManager getMessagingManager()
    {
        return this.manager;
    }

    /** Retrieve the <code>MessageSink</code>.
     *
     *  @return The message-sink.
     */
    public MessageSink getMessageSink()
    {
        return this.sink;
    }

    /** @see Registration
     */
    public MessageType getMessageType()
    {
        return this.messageType;
    }

    /** @see Registration
     */
    public void unregister()
    {
        getMessagingManager().unregister( this );
    }

    /** @see Registration
     */
    public Message getMessage(String id)
        throws NoSuchMessageException
    {
        Message message = (Message) this.messages.get( id );

        if ( message == null )
        {
            throw new NoSuchMessageException( id );
        }

        return message;
    }

    /** @see Registration
     */
    public void consumeMessage(String id)
        throws NoSuchMessageException
    {
        if ( ! this.messages.containsKey( id ) )
        {
            throw new NoSuchMessageException( id );
        }
        
        this.messages.remove( id );
    }

    /** Accept a message for potential delivery to the <code>MessageSink</code>.
     *
     *  <p>
     *  The message-type's message-selector is applied, and if matched, a reference
     *  to the message is provided to the message-sink.
     *  </p>
     *  
     *  @param message The message.
     */
    public void acceptMessage(Object message)
        throws Exception
    {
        if ( ((SimpleMessageSelector)getMessageType().getMessageSelector()).selects( message ) )
        {
            String msgId = "msg." + (++this.idCounter);

            Message messageWrapper = new DefaultMessage( msgId,
                                                         getMessageType(),
                                                         message );

            this.messages.put( msgId,
                               messageWrapper );

            getMessageSink().acceptMessage( messageWrapper );
        }
    }

}
