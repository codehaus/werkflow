package org.codehaus.werkflow.syntax.fundamental;

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
import org.codehaus.werkflow.definition.DuplicateMessageTypeException;
import org.codehaus.werkflow.service.messaging.MessageSelector;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

/** Define a <code>MessageType</code>.
 *
 *  <p>
 *  A &lt;message-type&gt; must be defined before being referenced
 *  in a &lt;message&gt; or &lt;message-initiator&gt;.  It must
 *  contain an <code>id</code> attribute and the body must contain
 *  tags to configure a <code>MessageSelector</code>.  It may optionally
 *  contain a &lt;documentation&gt; element.
 *  </p>
 *
 *  <p>
 *  <pre>
 *  &lt;message-type id="my.small.msg"&gt;
 *    &lt;documentation&gt;
 *      This is my small message.
 *    &lt;/documentation&gt;
 *    &lt:java:selector type="com.myco.Message" filter="${message.size &lt; 100}"/>
 *  &lt;/message-type&gt;
 *
 *  @see org.codehaus.werkflow.syntax.petri.MessageTag
 *  @see MessageSelector
 *  @see DocumentationTag
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class MessageTypeTag
    extends FundamentalTagSupport
    implements DocumentableTag
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Identifier. */
    private String id;

    /** Documentation, possibly null. */
    private String documentation;

    /** Message-selector. */
    private MessageSelector selector;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public MessageTypeTag()
    {
        // intentionally left blank.
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Set the identifier.
     *
     *  @param id The identifier.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /** Retrieve the identifier.
     *
     *  @return The identifier.
     */
    public String getId()
    {
        return this.id;
    }

    /** @see DocumentableTag
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
     *  @param selector The message-selector.
     */
    public void setMessageSelector(MessageSelector selector)
    {
        this.selector = selector;
    }

    /** Retrieve the <code>MessageSelector</code>.
     *
     *  @return The message-selector.
     */
    public MessageSelector getMessageSelector()
    {
        return this.selector;
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


        MessageType messageType = new MessageType( getId() );

        setDocumentation( null );

        pushObject( MessageTypeTag.class,
                    this );

        invokeBody( output );

        popObject( MessageTypeTag.class );

        messageType.setDocumentation( getDocumentation() );
        messageType.setMessageSelector( getMessageSelector() );

        try
        {
            getCurrentScope().addMessageType( messageType );
        }
        catch (DuplicateMessageTypeException e)
        {
            throw new JellyTagException( e );
        }
    }
}
