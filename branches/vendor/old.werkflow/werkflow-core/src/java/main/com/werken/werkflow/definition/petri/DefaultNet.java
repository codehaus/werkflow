package com.werken.werkflow.definition.petri;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class DefaultNet
    implements Net
{
    private Map places;
    private Map transitions;
    private Set arcs;

    public DefaultNet()
    {
        this.places      = new HashMap();
        this.transitions = new HashMap();
        this.arcs        = new HashSet();
    }

    public DefaultPlace addPlace(String id)
        throws DuplicateIdException
    {
        checkUniqueId( id );

        DefaultPlace place = new DefaultPlace( id );

        this.places.put( id,
                         place );

        return place;
    }

    public DefaultPlace getPlace(String id)
        throws NoSuchPlaceException
    {
        if ( ! this.places.containsKey( id ) )
        {
            throw new NoSuchPlaceException( this,
                                            id );
        }

        return (DefaultPlace) this.places.get( id );
    }

    public Place[] getPlaces()
    {
        return (Place[]) this.places.values().toArray( Place.EMPTY_ARRAY );
    }

    public DefaultTransition addTransition(String id)
        throws DuplicateIdException
    {
        checkUniqueId( id );

        DefaultTransition transition = new DefaultTransition( id );

        this.transitions.put( id,
                              transition );

        return transition;
    }

    public DefaultTransition getTransition(String id)
        throws NoSuchTransitionException
    {
        if ( ! this.transitions.containsKey( id ) )
        {
            throw new NoSuchTransitionException( this,
                                                 id );
        }

        return (DefaultTransition) this.transitions.get( id );
    }

    public Transition getTransitionById(String id)
        throws NoSuchTransitionException
    {
        return getTransition( id );
    }
    
    public Transition[] getTransitions()
    {
        return (Transition[]) this.transitions.values().toArray( Transition.EMPTY_ARRAY );
    }

    public Arc[] getArcs()
    {
        return (Arc[]) this.arcs.toArray( Arc.EMPTY_ARRAY );
    }

    public DefaultArc connectPlaceToTransition(String placeId,
                                               String transitionId)
        throws DuplicateIdException, NoSuchNodeException
    {
        DefaultPlace place = getPlace( placeId );
        DefaultTransition transition = getTransition( transitionId );

        DefaultArc arc = new DefaultArc( place,
                                         transition );

        place.addOutboundArc( arc );
        transition.addInboundArc( arc );

        this.arcs.add( arc );

        return arc;
    }

    public DefaultArc connectTransitionToPlace(String transitionId,
                                               String placeId)
        throws DuplicateIdException, NoSuchNodeException
    {
        DefaultTransition transition = getTransition( transitionId );
        DefaultPlace place = getPlace( placeId );

        DefaultArc arc = new DefaultArc( place,
                                         transition );

        transition.addOutboundArc( arc );
        place.addInboundArc( arc );

        this.arcs.add( arc );

        return arc;
    }

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
