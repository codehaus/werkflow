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

import java.util.Map;
import java.util.HashMap;

/** Library of <code>MessageType</code>s.
 *
 *  <p>
 *  <code>MessageTypeLibrary</code> instances may have parents
 *  and ancestors that are used for lookups while only allowing
 *  additions to occur at the leafs.  This creates effective
 *  scopes of <code>MessageType</code> definitions.
 *  </p>
 *
 *  @see MessageType
 *
 *  @author <A href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class MessageTypeLibrary
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Parent library, possibly null. */
    private MessageTypeLibrary parent;

    /** Message-types, indexed by id. */
    private Map messageTypes;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public MessageTypeLibrary()
    {
        this( null );
    }

    /** Construct with a parent.
     *
     *  @param parent The parent library.
     */
    public MessageTypeLibrary(MessageTypeLibrary parent)
    {
        this.parent = parent;
        this.messageTypes = new HashMap();
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Retrieve the parent <code>MessageTypeLibrary</code> if any.
     *
     *  @return The parent library or <code>null</code> if none.
     */
    public MessageTypeLibrary getParent()
    {
        return this.parent;
    }

    /** Determine if this library or its ancestors contains a
     *  <code>MessageType</code> with a matching identifier.
     *
     *  @param messageType The message-type to test.
     *
     *  @return <code>true</code> if a matching message-type
     *          is found, otherwise <code>false</code>.
     */
    public boolean containsMessageType(MessageType messageType)
    {
        if ( this.messageTypes.containsKey( messageType.getId() ) )
        {
            return true;
        }

        if ( this.parent != null )
        {
            return this.parent.containsMessageType( messageType );
        }

        return false;
    }

    /** Add a <code>MessageType</code> to this library.
     *
     *  @see #containsMessageType
     *
     *  @param messageType The message-type to add.
     *
     *  @throws DuplicateMessageTypeException If the identifier of
     *          the message-type conflicts with previous entries in
     *          either this library or its ancestors.
     */
    public void addMessageType(MessageType messageType)
        throws DuplicateMessageTypeException
    {
        if ( containsMessageType( messageType ) )
        {
            throw new DuplicateMessageTypeException( messageType );
        }

        this.messageTypes.put( messageType.getId(),
                               messageType );
    }

    /** Retrieve a <code>MessageType</code> by identifier.
     *
     *  @param id The message-type identifier.
     *
     *  @return The message-type.
     *
     *  @throws NoSuchMessageTypeException If no message-type is
     *          associated with the specified identifier.
     */
    public MessageType getMessageType(String id)
        throws NoSuchMessageTypeException
    {
        if ( ! this.messageTypes.containsKey( id ) )
        {
            if ( this.parent == null )
            {
                throw new NoSuchMessageTypeException( id );
            }

            return this.parent.getMessageType( id );
        }

        return (MessageType) this.messageTypes.get( id );
    }
}
