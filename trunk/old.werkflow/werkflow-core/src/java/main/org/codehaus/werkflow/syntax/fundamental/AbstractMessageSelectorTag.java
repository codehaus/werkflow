package com.werken.werkflow.syntax.fundamental;

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
import com.werken.werkflow.jelly.MiscTagSupport;

import org.apache.commons.jelly.JellyTagException;

/** Base for custom <code>MessageSelector</code> tags.
 *
 *  <p>
 *  Due to the pluggable nature of werkflow semantics, the
 *  <code>AbstractMessageSelectorTag</code> assists in weaving custom
 *  <code>MessageSelector</code>s into the fundamental syntax.
 *  </p>
 *
 *  <p>
 *  The custom tag should perform whatever is required to
 *  construct an <code>MessageSelector</code> in the {@link org.apache.commons.jelly.Tag#doTag}
 *  method, and finally call {@link #setMessageSelector} on this class
 *  to install the message-selector into the appropriate library or task.
 *  </p>
 *
 *  @see MessageSelector
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public abstract class AbstractMessageSelectorTag
    extends MiscTagSupport
{
    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public AbstractMessageSelectorTag()
    {
        // intentionally left blank
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Install a <code>MessageSelector</code>.
     *
     *  @param selector The message-selector.
     *
     *  @throws JellyTagException If the tag is not used within the correct
     *          context of a &lt;message-type&gt; tag.
     */
    public void setMessageSelector(MessageSelector selector)
        throws JellyTagException
    {
        MessageTypeTag messageType = (MessageTypeTag) findAncestorWithClass( MessageTypeTag.class );

        if ( messageType == null )
        {
            try
            {
                messageType = (MessageTypeTag) peekObject( MessageTypeTag.class );
            }
            catch (JellyTagException e)
            {
                // swallow
            }
        }

        if ( messageType == null )
        {
            throw new JellyTagException( "not within <message-type>" );
        }

        messageType.setMessageSelector( selector );
    }
}
