package com.werken.werkflow.syntax.petri;

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

import com.werken.werkflow.definition.MessageCorrelator;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.NoSuchMessageTypeException;
import com.werken.werkflow.syntax.fundamental.MessageCorrelatorReceptor;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

/** Create a message-trigger on a <code>Transition</code>.
 *
 *  <p>
 *  A &lt;message&gt may be nested inside of a &lt;transition&gt;
 *  to designate the transition as requiring a message in order
 *  to be fired.  The message specifies a type and may optionally
 *  contain an arbitrary correlator as the body.
 *  </p>
 *
 *  <p>
 *  <pre>
 *  &lt;message type="some.msg.type" id="myMsg"&gt;
 *    &lt;jelly:correlator test="${myMsg.foo = bar}"/&gt;
 *  &lt;/message&gt;
 *  </pre>
 *  </p>
 *
 *  @see TransitionTag
 *  @see MessageTypeTag
 *  @see com.werken.werkflow.semantics.jelly.JellyMessageCorrelatorTag
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class MessageTag
    extends PetriTagSupport
    implements MessageCorrelatorReceptor
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Message-type identifier. */
    private String messageTypeId;

    /** Binding attr. */
    private String id;

    /** Correlator, possibly null. */
    private MessageCorrelator correlator;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public MessageTag()
    {
        // intentionally left blank
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Set the <code>MessageType</code> identifier.
     *
     *  @param messageTypeId The identifier.
     */
    public void setType(String messageTypeId)
    {
        this.messageTypeId = messageTypeId;
    }

    /** Retrieve the <code>MessageType</code> identifier.
     *
     *  @return The identifier.
     */
    public String getType()
    {
        return this.messageTypeId;
    }

    /** Set the message identifier.
     *
     *  @param id The identifier.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /** Retrieve the message identifier.
     *
     *  @return The identifier.
     */
    public String getId()
    {
        return this.id;
    }

    /** @see MessageCorrelatorReceptor
     */
    public String getMessageId()
    {
        return getId();
    }

    /** @see MessageCorrelatorReceptor
     */
    public void receiveMessageCorrelator(MessageCorrelator correlator)
    {
        this.correlator = correlator;
    }

    /** Retrieve the <code>MessageCorrelator</code>.
     *
     *  @return The message-correlator.
     */
    public MessageCorrelator getMessageCorrelator()
    {
        return this.correlator;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** @see org.apache.commons.jelly.Tag
     */
    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireStringAttribute( "id",
                                getId() );

        requireStringAttribute( "type",
                                getType() );

        DefaultTransition transition = getCurrentTransition();

        MessageType msgType = null;

        try
        {
            msgType = getCurrentScope().getMessageType( getType() );
        }
        catch (NoSuchMessageTypeException e)
        {
            throw new JellyTagException( e );
        }

        MessageWaiter waiter = new MessageWaiter( msgType,
                                                  getId() );

        transition.setWaiter( waiter );

        invokeBody( output );

        if ( getMessageCorrelator() != null )
        {
            waiter.setMessageCorrelator( getMessageCorrelator() );
        }
    }
}
