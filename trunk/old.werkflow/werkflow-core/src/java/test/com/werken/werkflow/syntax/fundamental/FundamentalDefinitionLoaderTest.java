package com.werken.werkflow.syntax.fundamental;

import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.Place;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.definition.petri.AndInputRule;
import com.werken.werkflow.definition.petri.OrInputRule;

import junit.framework.TestCase;

import java.net.URL;

public class FundamentalDefinitionLoaderTest
    extends TestCase
{
    public void testLoad()
        throws Exception
    {
        FundamentalDefinitionLoader loader = new FundamentalDefinitionLoader();

        URL url = getClass().getResource( "process-1.xml" );

        ProcessDefinition[] defs = loader.load( url );

        assertEquals( 1,
                      defs.length );

        assertEquals( "process one",
                      defs[0].getId() );

        Net net = defs[0].getNet();

        assertNotNull( net );

        assertEquals( 3,
                      net.getPlaces().length );

        Place place = getPlace( "in",
                                net.getPlaces() );

        assertNotNull( place );

        assertEquals( "the in place",
                      place.getDocumentation() );

        place = getPlace( "middle",
                          net.getPlaces() );

        assertNotNull( place );

        assertEquals( "the middle place",
                      place.getDocumentation() );

        place = getPlace( "out",
                          net.getPlaces() );
        
        assertNotNull( place );
        
        assertEquals( "the out place",
                      place.getDocumentation() );

        assertEquals( 2,
                      net.getTransitions().length );

        Transition transition = getTransition( "transition one",
                                               net.getTransitions() );

        assertNotNull( transition );

        assertEquals( "the 1st transition",
                      transition.getDocumentation() );

        assertTrue( transition.getActivationRule() instanceof OrInputRule );

        transition = getTransition( "transition two",
                                    net.getTransitions() );

        assertNotNull( transition );

        assertEquals( "the 2nd transition",
                      transition.getDocumentation() );

        assertTrue( transition.getActivationRule() instanceof AndInputRule );
    }

    protected Place getPlace(String placeId,
                             Place[] places)
    {
        for ( int i = 0 ; i < places.length ; ++i )
        {
            if ( places[i].getId().equals( placeId ) )
            {
                return places[i];
            }
        }

        return null;
    }

    protected Transition getTransition(String transitionId,
                                       Transition[] transitions)
    {
        for ( int i = 0 ; i < transitions.length ; ++i )
        {
            if ( transitions[i].getId().equals( transitionId ) )
            {
                return transitions[i];
            }
        }

        return null;
    }
}
