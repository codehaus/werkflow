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

import com.werken.werkflow.NoSuchCaseException;
import com.werken.werkflow.NoSuchProcessException;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.MessageCorrelator;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.IncompatibleMessageSelectorException;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

/** Master message correlator.
 *
 *  <p>
 *  Message correlation is a hierarchic affair.  The <code>Correlator</code>
 *  dispatches to a <code>MessageTypeCorrelator</code> which in terms
 *  disptaches to a <code>MessageWaiterCorrelator</code>.
 *  </p>
 *
 *  @see MessageTypeCorrelator
 *  @see MessageWaiterCorrelator
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class Correlator
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Workflow engine. */
    private WorkflowEngine engine;

    /** Owning process deployment. */
    private ProcessDeployment deployment;

    /** Message-type correlators, indexed by message-type id. */
    private Map msgTypeCorrelators;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param engine The workflow engine.
     *  @param deployment The owning process deployment.
     */
    public Correlator(WorkflowEngine engine,
                      ProcessDeployment deployment)
    {
        this.engine             = engine;
        this.deployment         = deployment;
        this.msgTypeCorrelators = new HashMap();
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

    /** Retrieve the owning <code>ProcessDeployment</code>.
     *
     *  @return The process deployment.
     */
    private ProcessDeployment getProcessDeployment()
    {
        return this.deployment;
    }

    /** Add a <code>MessageWaiter</code> for a transition.
     *
     *  @param transitionId The transition identifier.
     *  @param messageWaiter The message-waiter.
     *
     *  @throws IncompatibleMessageSelectorException If the transition's
     *          message-type uses an incompatible message-selector.
     */
    void addMessageWaiter(String transitionId,
                          MessageWaiter messageWaiter)
        throws IncompatibleMessageSelectorException
    {
        MessageType messageType = messageWaiter.getMessageType();

        MessageTypeCorrelator msgTypeCorrelator = getOrCreateMessageTypeCorrelator( messageType );

        msgTypeCorrelator.addMessageWaiter( transitionId,
                                            messageWaiter );
    }

    /** Retrieve or create a <code>MessageTypeCorrelator</code> for
     *  a <code>MessageType</code>.
     *
     *  @param messageType
     *
     *  @return The message-type correlator.
     *
     *  @throws IncompatibleMessageSelectorException If the message-type
     *          uses an incompatible message-selector.
     */
    MessageTypeCorrelator getOrCreateMessageTypeCorrelator(MessageType messageType)
        throws IncompatibleMessageSelectorException
    {
        MessageTypeCorrelator correlator = getMessageTypeCorrelator( messageType );

        if ( correlator == null )
        {
            correlator = new MessageTypeCorrelator( getEngine(),
                                                    messageType );

            this.msgTypeCorrelators.put( messageType.getId(),
                                         correlator );
        }

        return correlator;
    }

    MessageTypeCorrelator getMessageTypeCorrelator(MessageType messageType)
    {
        return (MessageTypeCorrelator) this.msgTypeCorrelators.get( messageType.getId() );
    }

    void evaluateCase(WorkflowProcessCase processCase,
                      String[] transitionIds)
    {
        Iterator msgTypeCorrelatorIter = this.msgTypeCorrelators.values().iterator();

        MessageTypeCorrelator eachMsgTypeCorrelator = null;

        while ( msgTypeCorrelatorIter.hasNext() )
        {
            eachMsgTypeCorrelator = (MessageTypeCorrelator) msgTypeCorrelatorIter.next();

            eachMsgTypeCorrelator.evaluateCase( processCase,
                                                transitionIds );
        }
    }

    boolean isCorrelated(String processCaseId,
                         Transition transition)
    {
        MessageWaiter msgWaiter = transition.getMessageWaiter();

        MessageTypeCorrelator msgTypeCorrelator = getMessageTypeCorrelator( msgWaiter.getMessageType() );

        return msgTypeCorrelator.isCorrelated( processCaseId,
                                               transition.getId() );
    }

    Object consumeMessage(String processCaseId,
                          Transition transition)
        throws NoSuchCorrelationException
    {
        MessageWaiter msgWaiter = transition.getMessageWaiter();

        MessageTypeCorrelator msgTypeCorrelator = getMessageTypeCorrelator( msgWaiter.getMessageType() );

        return msgTypeCorrelator.consumeMessage( processCaseId,
                                                 transition.getId() );
    }
}

