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

import com.werken.werkflow.action.Action;
import com.werken.werkflow.definition.MessageInitiator;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.MessageSink;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.NoSuchMessageException;
import com.werken.werkflow.service.messaging.IncompatibleMessageSelectorException;

import java.util.Map;
import java.util.HashMap;

/** Process instance initiator.
 *
 *  @see MessageInitiator
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class Initiator
    implements MessageSink
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Workflow engine. */
    private WorkflowEngine engine;

    /** Owning process deployment. */
    private ProcessDeployment deployment;

    /** Available message initiators. */
    private Map messageInitiators;

    /** Message registrations. */
    private Map registrations;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param engine The workflow engine.
     *  @param deployment The owning process-deployment.
     */
    public Initiator(WorkflowEngine engine,
                     ProcessDeployment deployment)
    {
        this.engine     = engine;
        this.deployment = deployment;

        this.messageInitiators = new HashMap();
        this.registrations     = new HashMap();
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
     *  @return The owning process-deployment.
     */
    private ProcessDeployment getProcessDeployment()
    {
        return this.deployment;
    }

    /** Add message initiators.
     *
     *  @param messageInitiators The message initiators.
     *
     *  @throws ProcessDeploymentException If an error occurs attempting
     *          to configure the messaging bus.
     */
    public void addMessageInitiators(MessageInitiator[] messageInitiators)
        throws ProcessDeploymentException
    {
        try
        {
            for ( int i = 0 ; i < messageInitiators.length ; ++i )
            {
                addMessageInitiator( messageInitiators[i] );
            }
        }
        catch (IncompatibleMessageSelectorException e)
        {
            // FIXME: unregister thingies.
            e.printStackTrace();
            throw new ProcessDeploymentException( e );
        }
    }

    /** Add a message initiator.
     *
     *  @param messageInitiator The message initiator.
     *
     *  @throws IncompatibleMessageSelectorException If the message-selector used
     *          by the message-initiator's message-type is incompatible with
     *          the messaging manager.
     */
    public void addMessageInitiator(MessageInitiator messageInitiator)
        throws IncompatibleMessageSelectorException
    {
        MessageType messageType = messageInitiator.getMessageType();

        Registration registration = getEngine().register( this,
                                                          messageType );

        this.registrations.put( messageType,
                                registration );

        this.messageInitiators.put( messageType,
                                    messageInitiator );
    }

    /** Retrieve the <code>MessageInitiator</code> for a <code>MessageType</code>.
     *
     *  @param messageType The message type.
     *
     *  @return The message-initiator or <code>null</code> if none.
     */
    public MessageInitiator getMessageInitiator(MessageType messageType)
    {
        return (MessageInitiator) this.messageInitiators.get( messageType );
    }

    /** Retrieve the <code>Registration</code> for a <code>MessageType</code>.
     *
     *  @param messageType The message type.
     *
     *  @return The reigstration or <code>null</code> if none.
     */
    public Registration getRegistration(MessageType messageType)
    {
        return (Registration) this.registrations.get( messageType );
    }

    /** @see MessageSink
     */
    public void acceptMessage(Message message)
    {
        MessageType messageType = message.getMessageType();

        MessageInitiator initiator = getMessageInitiator( messageType );

        if ( initiator != null )
        {
            Registration registration = getRegistration( messageType );

            try
            {
                registration.consumeMessage( message.getId() );

                Map caseAttrs = new HashMap();
                Map otherAttrs = new HashMap();

                Action action = initiator.getAction();

                if ( action != null )
                {
                    caseAttrs.put( initiator.getBindingVar(),
                                   message.getMessage() );

                    otherAttrs.put( initiator.getBindingVar(),
                                    message.getMessage() );

                    InitiatorActivity activity = new InitiatorActivity( getEngine(),
                                                                        getProcessDeployment().getId(),
                                                                        caseAttrs );

                    action.perform( activity,
                                    caseAttrs,
                                    otherAttrs );
                }
            }
            catch (NoSuchMessageException e)
            {
                // FIXME
                e.printStackTrace();
            }
        }
    }
}
