package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.DefaultNet;
import com.werken.werkflow.definition.petri.DefaultPlace;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.DefaultArc;
import com.werken.werkflow.definition.petri.AndInputRule;
import com.werken.werkflow.definition.petri.PetriException;

public class NetBuilder
{
    private DefaultNet net;
    private long transitionCounter;
    private long placeCounter;

    private DefaultPlace in;
    private DefaultPlace out;

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

        this.in = this.net.addPlace( "in" );
        this.out = this.net.addPlace( "out" );

        DefaultPlace segOut = segment.append( this.in,
                                              this );
        
        connect( segOut,
                 out );

        this.net = null;

        return result;
    }

    public DefaultPlace getIn()
    {
        return this.in;
    }

    public DefaultPlace getOut()
    {
        return this.out;
    }

    public DefaultTransition newTransition()
        throws PetriException
    {
        DefaultTransition transition = this.net.addTransition( getNextTransitionId() );

        transition.setActivationRule( AndInputRule.getInstance() );

        return transition;
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

    public void connect(DefaultPlace first,
                        DefaultPlace last)
        throws PetriException
    {
        DefaultTransition transition = newTransition();

        connect( first,
                 transition );
        
        connect( transition,
                 last );
    }

    public void connect(DefaultTransition first,
                        DefaultTransition last)
        throws PetriException
    {
        DefaultPlace place = newPlace();

        connect( first,
                 place );

        connect( place,
                 last );
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
