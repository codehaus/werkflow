package com.werken.werkflow.syntax.fundamental;

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
import com.werken.werkflow.jelly.MiscTagSupport;

import org.apache.commons.jelly.JellyTagException;

/** Base for custom <code>MessageCorrelator</code> tags.
 *
 *  <p>
 *  Due to the pluggable nature of werkflow semantics, the
 *  <code>AbstractMessageCorrelatorTag</code> assists in weaving custom
 *  <code>MessageCorrelators</code>s into the fundamental syntax.
 *  </p>
 *
 *  <p>
 *  The custom tag should perform whatever is required to
 *  construct an <code>MessageCorrelator</code> in the {@link org.apache.commons.jelly.Tag#doTag}
 *  method, and finally call {@link #setMessageCorrelator} on this class
 *  to install the correlator.
 *  </p>
 *
 *  @see MessageCorrelator
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public abstract class AbstractMessageCorrelatorTag
    extends MiscTagSupport
{
    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public AbstractMessageCorrelatorTag()
    {
        // intentionally left blank
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Install a <code>MessageCorrelator</code>.
     *
     *  @param correlator The message correlator.
     *
     *  @throws JellyTagException If the tag is not used within the correct
     *          context of a &lt;message&gt; tag.
     */
    public void setMessageCorrelator(MessageCorrelator correlator)
        throws JellyTagException
    {
        MessageCorrelatorReceptor receptor = (MessageCorrelatorReceptor) findAncestorWithClass( MessageCorrelatorReceptor.class );

        if ( receptor == null )
        {
            try
            {
                receptor = (MessageCorrelatorReceptor) peekObject( MessageCorrelatorReceptor.class );
            }
            catch (JellyTagException e)
            {
                // swallow
            }
        }

        if ( receptor == null )
        {
            throw new JellyTagException( "message correlator not allowed in this context" );
        }

        receptor.receiveMessageCorrelator( correlator );
    }

    /** Retrieve the message identifier.
     *
     *  @return The message identifier.
     *
     *  @throws JellyTagException If the tag is not used within the correct
     *          context of a &lt;message&gt; tag.
     */
    public String getMessageId()
        throws JellyTagException
    {
        MessageCorrelatorReceptor receptor = (MessageCorrelatorReceptor) findAncestorWithClass( MessageCorrelatorReceptor.class );

        if ( receptor == null )
        {
            try
            {
                receptor = (MessageCorrelatorReceptor) peekObject( MessageCorrelatorReceptor.class );
            }
            catch (JellyTagException e)
            {
                // swallow
            }
        }

        if ( receptor == null )
        {
            throw new JellyTagException( "message correlator not allowed in this context" );
        }

        return receptor.getMessageId();
    }
}
