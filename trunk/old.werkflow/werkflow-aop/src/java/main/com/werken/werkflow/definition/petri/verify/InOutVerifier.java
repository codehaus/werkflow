package com.werken.werkflow.definition.petri.verify;

import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.Place;

public class InOutVerifier
    implements Verifier
{
    public InOutVerifier()
    {

    }

    public void verify(Net net)
        throws VerificationException
    {
        Place[] places = net.getPlaces();

        boolean inFound  = false;
        boolean outFound = false;

        for ( int i = 0 ; i < places.length ; ++i )
        {
            if ( places[i].getId().equals( "in" ) )
            {
                inFound = true;
            }
            else if ( places[i].getId().equals( "out" ) )
            {
                outFound = true;
            }

            if ( inFound
                 &&
                 outFound )
            {
                break;
            }
        }

        if ( ( ! inFound )
             &&
             ( ! outFound ) )
        {
            MultiVerificationException e = new MultiVerificationException( net );

            e.addVerificationException( new NoInPlaceException( net ) );
            e.addVerificationException( new NoOutPlaceException( net ) );

            throw e;
        }

        if ( ! inFound )
        {
            throw new NoInPlaceException( net );
        }

        if ( ! outFound )
        {
            throw new NoOutPlaceException( net );
        }
    }
}
