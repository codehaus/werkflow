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

import com.werken.werkflow.action.Action;
import com.werken.werkflow.definition.MessageInitiator;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.NoSuchMessageTypeException;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

/** Define a <code>MessageInitiator</code>.
 *
 *  @see MessageInitiator
 *  @see MessageType
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class MessageInitiatorTag
    extends FundamentalTagSupport
    implements DocumentableTag, ActionReceptor
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Message binding identifier. */
    private String id;

    /** Message-type identifier. */
    private String type;

    /** Documentation, possibly null. */
    private String documentation;

    /** Initialization action, possibly null. */
    private Action action;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public MessageInitiatorTag()
    {
        // intentionally left blank
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Set the message binding variable identifier.
     *
     *  @param id The binding variable identifier.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /** Retrieve the message binding variable identifier.
     *
     *  @return The binding variable identifier.
     */
    public String getId()
    {
        return this.id;
    }

    /** Set the message-type identifier.
     *
     *  @param type The message-type identifier.
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /** Retrieve the message-type identifier.
     *
     *  @return The message-type identifier.
     */
    public String getType()
    {
        return this.type;
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

    /** Set the initialization action.
     *
     *  @param action The initialization action.
     */
    public void setAction(Action action)
    {
        this.action = action;
    }

    /** @see ActionReceptor
     */
    public void receiveAction(Action action)
    {
        setAction( action );
    }

    /** Retrieve the initialization action.
     *
     *  @return The initialization action.
     */
    public Action getAction()
    {
        return this.action;
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

        ProcessTag process = (ProcessTag) requiredAncestor( "process",
                                                            ProcessTag.class );

        setDocumentation( null );

        invokeBody( output );

        MessageType messageType = null;

        try
        {
            messageType = getMessageTypeLibrary().getMessageType( getType() );
        }
        catch (NoSuchMessageTypeException e)
        {
            throw new JellyTagException( e );
        }

        MessageInitiator initiator = new MessageInitiator( messageType,
                                                           getId() );

        initiator.setDocumentation( getDocumentation() );

        if ( this.action != null )
        {
            initiator.setAction( this.action );
        }

        process.addMessageInitiator( initiator );
    }
}
