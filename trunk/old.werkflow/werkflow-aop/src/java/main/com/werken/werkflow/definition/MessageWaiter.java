package com.werken.werkflow.definition;

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

/** Annotation to a <code>Transition</code> to signal the requirement
 *  of a message.
 *
 *  <p>
 *  A <code>Transition</code> may not be firable until a correlated
 *  message arrives for a process-case.  The <code>MessageWaiter</code>
 *  is used to denote this requirement.
 *  </p>
 *
 *  @see MessageType
 *  @see MessageCorrelator
 *  @see com.werken.werkflow.definition.petri.Transition#getMessageWaiter
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class MessageWaiter
    implements Waiter
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Message-type. */
    private MessageType messageType;

    /** Correlator. */
    private MessageCorrelator messageCorrelator;

    /** Variable binding name for other-attributes. */
    private String bindingVar;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param messageType The message type.
     *  @param bindingVar The other-attributes binding variable name.
     */
    public MessageWaiter(MessageType messageType,
                         String bindingVar)
    {
        this.messageType = messageType;
        this.bindingVar    = bindingVar;
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Retrieve the <code>MessageType</code>.
     *
     *  @return The message type.
     */
    public MessageType getMessageType()
    {
        return this.messageType;
    }

    /** Retrieve the other-attributes binding variable name.
     *
     *  @return The binding variable name.
     */
    public String getBindingVar()
    {
        return this.bindingVar;
    }

    /** Set the <code>MessageCorrelator</code>.
     *
     *  @param messageCorrelator The message correlator.
     */
    public void setMessageCorrelator(MessageCorrelator messageCorrelator)
    {
        this.messageCorrelator = messageCorrelator;
    }

    /** Retrieve the <code>MessageCorrelator</code>.
     *
     *  @return The message correlator.
     */
    public MessageCorrelator getMessageCorrelator()
    {
        return this.messageCorrelator;
    }
}
