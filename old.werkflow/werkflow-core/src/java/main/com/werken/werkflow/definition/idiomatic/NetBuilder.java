package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.DefaultNet;
import com.werken.werkflow.definition.petri.DefaultPlace;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.DefaultArc;
import com.werken.werkflow.definition.petri.PetriException;

public class NetBuilder
{
    private DefaultNet net;
    private long transitionCounter;
    private long placeCounter;

    public NetBuilder()
    {
    }

    public synchronized Net build(Segment segment)
        throws PetriException
    {
        this.transitionCounter = 0;
        this.placeCounter      = 0;

        this.net = new DefaultNet();
        Net result = this.net;

        DefaultTransition[] ends = segment.build( this );


        DefaultPlace in = this.net.addPlace( "in" );
        DefaultPlace out = this.net.addPlace( "out" );

        connect( in,
                 ends[0] );

        
        connect( ends[1],
                 out );

        this.net = null;

        return result;
    }

    public DefaultTransition newTransition()
        throws PetriException
    {
        return this.net.addTransition( getNextTransitionId() );
    }

    public DefaultPlace newPlace()
        throws PetriException
    {
        return this.net.addPlace( getNextPlaceId() );
    }

    public DefaultArc connect(DefaultPlace place,
                              DefaultTransition transition)
        throws PetriException
    {
        return this.net.connectPlaceToTransition( place.getId(),
                                                  transition.getId() );
    }

    public DefaultArc connect(DefaultTransition transition,
                              DefaultPlace place)
        throws PetriException
    {
        return this.net.connectTransitionToPlace( transition.getId(),
                                                  place.getId() );
    }

    public String getNextTransitionId()
    {
        return "transition." + (++transitionCounter);
    }

    public String getNextPlaceId()
    {
        return "place." + (++placeCounter);
    }
}
