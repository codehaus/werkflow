package com.werken.werkflow.definition.petri.verify;

import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.Place;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.definition.petri.Arc;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class ConnectednessVerifier
    implements Verifier
{
    public ConnectednessVerifier()
    {

    }

    public void verify(Net net)
        throws VerificationException
    {
        Place[] places = net.getPlaces();

        Place in  = findPlace( "in",
                               places );

        Place out = findPlace( "out",
                               places );

        Set knownConnected = new HashSet();

        knownConnected.add( out );

        for ( int i = 0 ; i < places.length ; ++i )
        {
            ensurePath( places[i],
                        out,
                        knownConnected );
        }

        if ( places.length != knownConnected.size() )
        {
            throw new VerificationException( net );
        }
    }

    protected void ensurePath(Place in,
                              Place out,
                              Set knownConnected)
    {
        LinkedList path = new LinkedList();

        path.addLast( in );

        ensurePath( in,
                    out,
                    knownConnected,
                    path );
    }

    protected boolean ensurePath(Place in,
                                 Place out,
                                 Set knownConnected,
                                 LinkedList currentPath)
    {
        if ( currentPath.getLast().equals( out ) )
        {
            knownConnected.addAll( currentPath );
            return true;
        }

        Set after = getPlacesAfter( (Place) currentPath.getLast() );

        Iterator afterIter = after.iterator();
        Place    eachAfter = null;

        while ( afterIter.hasNext() )
        {
            eachAfter = (Place) afterIter.next();

            if ( knownConnected.contains( eachAfter ) )
            {
                knownConnected.addAll( currentPath );
                return true;
            }

            if ( ! currentPath.contains( eachAfter ) )
            {
                currentPath.addLast( eachAfter );

                boolean result = ensurePath( in,
                                             out,
                                             knownConnected,
                                             currentPath );

                currentPath.removeLast();

                if ( result )
                {
                    return true;
                }
            }
        }

        return false;
    }

    protected Set getPlacesBefore(Place place)
    {
        Set places = new HashSet();

        Arc[] arcsFromTransitions = place.getArcsFromTransitions();

        for ( int i = 0 ; i < arcsFromTransitions.length ; ++i )
        {
            Transition transition = arcsFromTransitions[i].getTransition();

            Arc[] arcsFromPlaces = transition.getArcsFromPlaces();

            for ( int j = 0 ; j < arcsFromPlaces.length ; ++j )
            {
                places.add( arcsFromPlaces[j].getPlace() );
            }
        }

        return places;
    }

    protected Set getPlacesAfter(Place place)
    {
        Set places = new HashSet();

        Arc[] arcsToTransitions = place.getArcsToTransitions();

        for ( int i = 0 ; i < arcsToTransitions.length ; ++i )
        {
            Transition transition = arcsToTransitions[i].getTransition();

            Arc[] arcsToPlaces = transition.getArcsToPlaces();

            for ( int j = 0 ; j < arcsToPlaces.length ; ++j )
            {
                places.add( arcsToPlaces[j].getPlace() );
            }
        }

        return places;
    }

    private Place findPlace(String id,
                            Place[] places)
    {
        for ( int i = 0 ; i < places.length ; ++i )
        {
            if ( places[i].getId().equals( id ) )
            {
                return places[i];
            }
        }

        return null;
    }
}
