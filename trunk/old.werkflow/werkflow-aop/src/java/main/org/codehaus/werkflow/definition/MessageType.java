package com.werken.werkflow.definition;

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

import com.werken.werkflow.service.messaging.MessageSelector;

/** Semantic message type.
 *
 *  <p>
 *  The application using werkflow defines the semantics for
 *  types of messages.  The <code>MessageType</code> provides
 *  a method for creating a new semantic message-type that
 *  uses a <code>MessageSelector</codd> for message differentiation.
 *  </p>
 *
 *  @see MessageSelector
 *  @see MessageInitiator
 *  @see MessageWaiter
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 *
 *  @todo MOVE TO services.messaging.*
 */
public class MessageType
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    /** Empty <code>MessageType</code> array. */
    public static final MessageType[] EMPTY_ARRAY = new MessageType[0];

    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Identifier. */
    private String id;

    /** Documentaiton, possibly null. */
    private String documentation;

    /** Message differentiator selector. */
    private MessageSelector selector;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param id The identifier.
     */
    public MessageType(String id)
    {
        this.id = id;
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Retrieve the identifier.
     *
     *  @return The identifier.
     */
    public String getId()
    {
        return this.id;
    }

    /** Set the documentation.
     *
     *  @param documentation The documentation.
     */
    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    /** Retrieve the documentation.
     *
     *  @return The documentation.
     */
    public String getDocumentation()
    {
        return this.documentation;
    }

    /** Set the <code>MessageSelector</code>.
     *
     *  @param selector The message selector.
     */
    public void setMessageSelector(MessageSelector selector)
    {
        this.selector = selector;
    }

    /** Retrieve the <code>MessageSelector</code>.
     *
     *  @return The message selector.
     */
    public MessageSelector getMessageSelector()
    {
        return this.selector;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /** @see java.lang.Object
     */
    public boolean equals(Object that)
    {
        if ( that instanceof MessageType )
        {
            return ( ((MessageType)that).getId().equals( getId() )
                     &&
                     ((MessageType)that).getMessageSelector().equals( getMessageSelector() ) );
        }

        return false;
    }

    /** @see java.lang.Object
     */
    public int hashCode()
    {
        return getId().hashCode() + getMessageSelector().hashCode();
    }
}
