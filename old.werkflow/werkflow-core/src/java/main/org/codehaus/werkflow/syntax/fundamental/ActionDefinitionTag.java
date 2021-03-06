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

import org.codehaus.werkflow.work.Action;
import org.codehaus.werkflow.work.DuplicateActionException;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.XMLOutput;

public class ActionDefinitionTag
    extends FundamentalTagSupport
    implements ActionReceptor
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Identifier. */
    private String id;

    /** Action definition, possibly null. */
    private Action action;

    /** Is the default action? */
    private boolean isDefault;

    // ----------------------------------------------------------------------
    //      Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public ActionDefinitionTag()
    {
        // intentionally left blank
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Set the identifier.
     *
     *  @param id The identifier.
     */
    public void setId( String id )
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

    public void setAction( Action action )
    {
        this.action = action;
    }

    /** @see ActionReceptor
     */
    public void receiveAction( Action action )
    {
        setAction( action );
    }

    /** Retrieve the <code>Action</code>.
     *
     *  @return The action.
     */
    public Action getAction()
    {
        return this.action;
    }

    /** Set the default flag.
     *
     *  @param isDefault Flag indicating if this is the default action.
     */
    public void setDefault( boolean isDefault )
    {
        this.isDefault = isDefault;
    }

    /** Get the default flag.
     *
     *  @return <code>true</code> if this is the default action,
     *          otherwise <code>false</code>.
     */
    public boolean isDefault()
    {
        return this.isDefault;
    }

    /** @see org.apache.commons.jelly.Tag
     */
    public void doTag( XMLOutput output )
        throws JellyTagException
    {
        requireStringAttribute( "id",
                                getId() );

        pushObject( ActionReceptor.class, this );

        invokeBody( output );

        popObject( ActionReceptor.class, getContext() );

        if ( getAction() == null )
        {
            throw new JellyTagException( "no action defined in body" );
        }

        try
        {
            getCurrentScope().addAction( getId(),
                                         getAction() );
        }
        catch ( DuplicateActionException e )
        {
            throw new JellyTagException( e );
        }

        if ( isDefault() )
        {
            getCurrentScope().setDefaultAction( getAction() );
        }
    }
}
