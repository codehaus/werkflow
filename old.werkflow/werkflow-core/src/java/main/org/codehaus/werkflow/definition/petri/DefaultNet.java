package org.codehaus.werkflow.definition.petri;

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
import java.util.Set;
import java.util.HashSet;

/** Default <code>Net</code> implementation.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class DefaultNet
    implements Net
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Places, by id. */
    private Map places;

    /** Transitions, by id. */
    private Map transitions;

    /** Arcs. */
    private Set arcs;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public DefaultNet()
    {
        this.places      = new HashMap();
        this.transitions = new HashMap();
        this.arcs        = new HashSet();
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Add a new <code>Place</code>.
     *
     *  @param id The place identifier.
     *
     *  @return The new place.
     *
     *  @throws DuplicateIdException If the identifier conflicts with existing
     *          place or transition identifiers.
     */
    public DefaultPlace addPlace(String id)
        throws DuplicateIdException
    {
        checkUniqueId( id );

        DefaultPlace place = new DefaultPlace( id );

        this.places.put( id,
                         place );

        return place;
    }

    /** Retrieve a <code>Place</code> by identifier.
     *
     *  @param id The place identifier.
     *
     *  @return The place.
     *
     *  @throws NoSuchPlaceException If no place is associated
     *          with the specified identifier.
     */
    public DefaultPlace getMutablePlace(String id)
        throws NoSuchPlaceException
    {
        if ( ! this.places.containsKey( id ) )
        {
            throw new NoSuchPlaceException( this,
                                            id );
        }

        return (DefaultPlace) this.places.get( id );
    }

    /** @see Net
     */
    public Place[] getPlaces()
    {
        return (Place[]) this.places.values().toArray( Place.EMPTY_ARRAY );
    }

    public Place getPlace(String id)
        throws NoSuchPlaceException
    {
        return getMutablePlace( id );
    }

    /** Add a new <code>Transition</code>.
     *
     *  @param id The transition identifier.
     *
     *  @return The new transition.
     *
     *  @throws DuplicateIdException If the identifier conflicts with existing
     *          place or transition identifiers.
     */
    public DefaultTransition addTransition(String id)
        throws DuplicateIdException
    {
        checkUniqueId( id );

        DefaultTransition transition = new DefaultTransition( id );

        this.transitions.put( id,
                              transition );

        return transition;
    }

    /** Retrieve a <code>Transition</code> by identifier.
     *
     *  @param id The transition identifier.
     *
     *  @return The transition.
     *
     *  @throws NoSuchTransitionException If no transition is associated
     *          with the specified identifier.
     */
    public DefaultTransition getMutableTransition(String id)
        throws NoSuchTransitionException
    {
        if ( ! this.transitions.containsKey( id ) )
        {
            throw new NoSuchTransitionException( this,
                                                 id );
        }

        return (DefaultTransition) this.transitions.get( id );
    }

    /** @see Net
     */
    public Transition getTransition(String id)
        throws NoSuchTransitionException
    {
        return getMutableTransition( id );
    }

    /** @see Net
     */
    public Transition[] getTransitions()
    {
        return (Transition[]) this.transitions.values().toArray( Transition.EMPTY_ARRAY );
    }

    /** @see Net
     */
    public Arc[] getArcs()
    {
        return (Arc[]) this.arcs.toArray( Arc.EMPTY_ARRAY );
    }

    /** Create an <code>Arc</code> from a <code>Place</code>
     *  to a <code>Transition</code>.
     *
     *  @param placeId The place identifier.
     *  @param transitionId The transition identifier.
     *
     *  @return The new connecting arc.
     *
     *  @throws NoSuchNodeException If either the place identifier or
     *          the transition identifier are invalid.
     */
    public DefaultArc connectPlaceToTransition(String placeId,
                                               String transitionId)
        throws NoSuchNodeException
    {
        DefaultPlace place = getMutablePlace( placeId );
        DefaultTransition transition = getMutableTransition( transitionId );

        DefaultArc arc = new DefaultArc( place,
                                         transition );

        place.addOutboundArc( arc );
        transition.addInboundArc( arc );

        this.arcs.add( arc );

        return arc;
    }

    /** Create an <code>Arc</code> from a <code>Transition</code>
     *  to a <code>Place</code>.
     *
     *  @param transitionId The transition identifier.
     *  @param placeId The place identifier.
     *
     *  @return The new connecting arc.
     *
     *  @throws NoSuchNodeException If either the transition identifier or
     *          the place identifier are invalid.
     */
    public DefaultArc connectTransitionToPlace(String transitionId,
                                               String placeId)
        throws NoSuchNodeException
    {
        DefaultTransition transition = getMutableTransition( transitionId );
        DefaultPlace place = getMutablePlace( placeId );

        DefaultArc arc = new DefaultArc( place,
                                         transition );

        transition.addOutboundArc( arc );
        place.addInboundArc( arc );

        this.arcs.add( arc );

        return arc;
    }

    /** Check that a potential identifier is unique.
     *
     *  @param id The candidate identifier.
     *
     *  @throws DuplicateIdException If duplicity-checking fails.
     */
    protected void checkUniqueId(String id)
        throws DuplicateIdException
    {
        if ( this.places.containsKey( id )
             ||
             this.transitions.containsKey( id ) )
        {
            throw new DuplicateIdException( this,
                                            id );
        }
    }
}
