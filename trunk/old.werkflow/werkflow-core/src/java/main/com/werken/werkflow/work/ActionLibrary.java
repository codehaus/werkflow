package com.werken.werkflow.work;

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

import java.util.Map;
import java.util.HashMap;

/** Hierarchic library of <code>Action</code>s.
 *
 *  <p>
 *  While not strictly required for using werkflow, the <code>ActionLibrary</code>
 *  is of use to process-definition syntax creators.
 *  </p>
 *
 *  <p>
 *  A <code>ActionLibrary</code> may be a part of a hierachy where new
 *  <code>Action</code>s are added directly to it, but <code>Action</code>s
 *  are automatically retrieved from the parentage chain, if needed.
 *  </p>
 *
 *  @see Action
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class ActionLibrary
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Parent library, possibly null. */
    private ActionLibrary parent;

    /** Actions indexed by id. */
    private Map actions;

    /** Default action, possibly null. */
    private Action defaultAction;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct a root library.
     */
    public ActionLibrary()
    {
        this( null );
    }

    /** Construct a library with a parent.
     *
     *  @param parent The parent.
     */
    public ActionLibrary( ActionLibrary parent )
    {
        this.parent = parent;
        this.actions = new HashMap();
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Retrieve the parent <code>ActionLibrary</code> if any.
     *
     *  @return The parent library or <code>null</code> if this library
     *          is a parent-less root library.
     */
    public ActionLibrary getParent()
    {
        return this.parent;
    }

    /** Determine if an <code>Action</code> with the given identifier
     *   exists within this library or its parentage chain.
     *
     *  @param id The action identifier to test.
     *
     *  @return <code>true</code> if this library or one of its parents
     *          contains an action associated with the identifier.
     */
    public boolean containsAction( String id )
    {
        if ( this.actions.containsKey( id ) )
        {
            return true;
        }

        if ( this.parent != null )
        {
            return this.parent.containsAction( id );
        }

        return false;
    }

    /** Add an action directly to this library.
     *
     *  <p>
     *  Even if this library has a parent, the action will be
     *  added only within the scope of this one.
     *  </p>
     *
     *  @see #getAction
     *
     *  @param id The action identifier.
     *  @param action The action.
     *
     *  @throws DuplicateActionException If the identifier is already
     *          associated with an action either directly within this
     *          library or one of the libraries along the parentage chain.
     */
    public void addAction( String id,
                           Action action )
        throws DuplicateActionException
    {
        if ( containsAction( id ) )
        {
            throw new DuplicateActionException( id );
        }

        this.actions.put( id,
                          action );
    }

    /** Retrieve an <code>Action</code> by identifier.
     *
     *  <p>
     *  This method searches this library and iteratively
     *  walks the ancestry chain until an action is found
     *  of the chain is exhausted.
     *  </p>
     *
     *  @param id The identifier.
     *
     *  @return The action.
     *
     *  @throws NoSuchActionException If no action is identified wtih
     *          an action either directly within this library or one of the
     *          libraries along the parentage chain.
     */
    public Action getAction( String id )
        throws NoSuchActionException
    {
        Action action = (Action) this.actions.get( id );

        // Try locally first.
        if ( action != null )
        {
            return action;
        }

        // Try the parent.
        if ( this.parent != null )
        {
            action = this.parent.getAction( id );

            if ( action != null )
            {
                return action;
            }
        }

        // See if a default action has been set.
        if ( getDefaultAction() != null )
        {
            return getDefaultAction();
        }

        // We're hosed eh :-)
        throw new NoSuchActionException( id );
    }

    /** Set the default <code>Action</code>.
     *
     *  @param defaultAction The default action.
     */
    public void setDefaultAction( Action defaultAction )
    {
        this.defaultAction = defaultAction;
    }

    /** Retrieve the default <code>Action</code>.
     *
     *  @return The default action.
     */
    public Action getDefaultAction()
    {
        if ( this.defaultAction != null )
        {
            return this.defaultAction;
        }

        if ( this.parent != null )
        {
            return this.parent.getDefaultAction();
        }

        return null;
    }
}
