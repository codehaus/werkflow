package com.werken.werkflow.engine;

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

import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.MessageSink;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.NoSuchMessageException;
import com.werken.werkflow.service.messaging.IncompatibleMessageSelectorException;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/** Correlator for messages of a paritcular <code>MessageType</code>.
 *
 *  @see Correlator
 *  @see MessageWaiterCorrelator
 *  @see MessageType
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class MessageTypeCorrelator
    implements MessageSink
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Workflow engine. */
    private WorkflowEngine engine;

    /** Message type. */
    private MessageType messageType;

    /** Message-waitier correlators, indexed by transition id. */
    private Map msgWaiterCorrelators;

    /** Message-source registration. */
    private Registration registration;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param engine The workflow engine.
     *  @param messageType The message-type.
     *
     *  @throws IncompatibleMessageSelectorException If the message-type
     *          uses a message-selector that is incompatible with the
     *          messaging-manager.
     */
    public MessageTypeCorrelator(WorkflowEngine engine,
                                 MessageType messageType)
        throws IncompatibleMessageSelectorException
    {
        this.engine               = engine;
        this.messageType          = messageType;
        this.msgWaiterCorrelators = new HashMap();

        this.registration = getEngine().register( this,
                                                  messageType );
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Retrieve the <code>WorkflowEngine</code>.
     *
     *  @return The engine.
     */
    private WorkflowEngine getEngine()
    {
        return this.engine;
    }

    /** Retrieve the <code>MessageType</code>.
     *
     *  @return The message-type.
     */
    public MessageType getMessageType()
    {
        return this.messageType;
    }

    /** Retrieve the message-source <code>Registration</code>.
     *
     *  @return The registration.
     */
    Registration getRegistration()
    {
        return this.registration;
    }

    /** Retrieve the <code>MessageWaiterCorrelator</code> for a transition.
     *
     *  @param transitionId The transition identifier.
     *
     *  @return The message-waiter correlator.
     */
    MessageWaiterCorrelator getMessageWaiterCorrelator(String transitionId)
    {
        return (MessageWaiterCorrelator) this.msgWaiterCorrelators.get( transitionId );
    }

    /** Retrieve a <code>Message</code>.
     *
     *  @param msgId The message identifier.
     *
     *  @return The message.
     *
     *  @throws NoSuchMessageException If no message is associated with
     *          the specified identifier.
     */
    Message getMessage(String msgId)
        throws NoSuchMessageException
    {
        return getRegistration().getMessage( msgId );
    }
    
    /** Add a <code>MessageWaiter</code> for a transition.
     *
     *  @param transitionId The transition identifier.
     *  @param messageWaiter The message waiter.
     */
    void addMessageWaiter(String transitionId,
                          MessageWaiter messageWaiter)
    {
        MessageWaiterCorrelator msgWaiterCorrelator = new MessageWaiterCorrelator( getEngine(),
                                                                                   this,
                                                                                   transitionId,
                                                                                   messageWaiter );

        this.msgWaiterCorrelators.put( transitionId,
                                       msgWaiterCorrelator );
    }

    /** @see MessageSink
     */
    public void acceptMessage(Message message)
    {
        Iterator correlatorIter = this.msgWaiterCorrelators.values().iterator();

        MessageWaiterCorrelator eachCorrelator = null;

        while ( correlatorIter.hasNext() )
        {
            eachCorrelator = (MessageWaiterCorrelator) correlatorIter.next();

            eachCorrelator.acceptMessage( message );
        }
    }

    /** Evaluate a <code>WorkflowProcessCase</code>.
     *
     *  @param processCase The process case.
     *  @param transitionIds The activated transitions.
     */
    void evaluateCase(WorkflowProcessCase processCase,
                      String[] transitionIds)
    {
        Iterator correlatorIter = this.msgWaiterCorrelators.values().iterator();

        MessageWaiterCorrelator eachCorrelator = null;

        while ( correlatorIter.hasNext() )
        {
            eachCorrelator = (MessageWaiterCorrelator) correlatorIter.next();

            eachCorrelator.evaluateCase( processCase,
                                         transitionIds );
        }
    }

    /** Determine if a message is correlated for a cases's transition.
     *
     *  @param processCaseId The process case identifier.
     *  @param transitionId The transition identifier.
     *
     *  @return <code>true</code> if a message is correlated,
     *          otherwise <code>false</code>.
     */
    boolean isCorrelated(String processCaseId,
                         String transitionId)
    {
        MessageWaiterCorrelator msgWaiterCorrelator = getMessageWaiterCorrelator( transitionId );

        return msgWaiterCorrelator.isCorrelated( processCaseId );
    }

    /** Consume a correlated message for a cases's transition.
     *
     *  @param processCaseId The process case identifier.
     *  @param transitionId The transition identifier.
     *
     *  @return The message object.
     *
     *  @throws NoSuchCorrelationException If no message is correalted.
     */
    Object consumeMessage(String processCaseId,
                          String transitionId)
        throws NoSuchCorrelationException
    {
        MessageWaiterCorrelator msgWaiterCorrelator = getMessageWaiterCorrelator( transitionId );

        return msgWaiterCorrelator.consumeMessage( processCaseId );
    }
}
