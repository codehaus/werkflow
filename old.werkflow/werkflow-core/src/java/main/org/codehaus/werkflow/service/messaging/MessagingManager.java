package org.codehaus.werkflow.service.messaging;

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

import org.codehaus.werkflow.definition.MessageType;

/** Service to provide inbound messages to werkflow.
 *
 *  <p>
 *  The <code>WorkflowEngine</code> by way of <code>WfmsServices</code>
 *  maintains access to a <code>MessagingManager</code>.  Each process
 *  deployed may register zero of more <code>MessageSink</code> and
 *  <code>MessageType</code> pairs with the <code>MessagingManager</code>
 *  in order to express an interest in receiving particular messages.
 *  </p>
 *
 *  <p>
 *  The {@link MessageSink#acceptMessage} method is called by
 *  implementations of <code>MessagingManager</code> had pass interesting
 *  messages to the <code>MessageSink</code>s that have expressed
 *  an interest.
 *  </p>
 *
 *  <p>
 *  Unregistering a message interest is accomplished by the client
 *  using the {@link Registration#unregister}.
 *  </p>
 *
 *  @see MessageType
 *  @see MessageSelector
 *  @see MessageSink
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public interface MessagingManager
{
    /** Component role. */
    public static final String ROLE = MessagingManager.class.getName();

    /** Register an interest by a <code>MessageSink</code> in
     *  messages matching a particular <code>MessageType</code>.
     *
     *  @param sink The message sink.
     *  @param messageType The message type.
     *
     *  @return The registration of the interest.
     *
     *  @throws IncompatibleMessageSelectorException if the
     *          message-selector of the message-type is not usable by
     *          the implementation of the messaging-manager.
     */
    Registration register(MessageSink sink,
                          MessageType messageType)
        throws IncompatibleMessageSelectorException;
}
