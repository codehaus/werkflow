package com.werken.werkflow.definition.petri;

import junit.framework.TestCase;

public class DefaultNetTest
    extends TestCase
{
    public void testConstruct()
    {
        DefaultNet net = new DefaultNet();

        assertEquals( 0,
                      net.getPlaces().length );

        assertEquals( 0,
                      net.getTransitions().length );

        assertEquals( 0,
                      net.getArcs().length );
    }

    public void testPlace()
        throws Exception
    {
        DefaultNet net = new DefaultNet();

        DefaultPlace place = net.addPlace( "the place" );

        assertSame( place,
                    net.getPlace( "the place" ) );

        assertEquals( 1,
                      net.getPlaces().length );

        assertSame( place,
                    net.getPlaces()[0] );

        try
        {
            net.getPlace( "no place" );
            fail( "should have thrown NoSuchPlaceException" );
        }
        catch (NoSuchPlaceException e)
        {
            // expected and correct

            assertEquals( "no place",
                          e.getId() );
        }
    }

    public void testTransition()
        throws Exception
    {
        DefaultNet net = new DefaultNet();

        DefaultTransition transition = net.addTransition( "the transition" );

        assertSame( transition,
                    net.getTransition( "the transition" ) );

        assertEquals( 1,
                      net.getTransitions().length );

        assertSame( transition,
                    net.getTransitions()[0] );

        try
        {
            net.getTransition( "no transition" );
            fail( "should have thrown NoSuchTransitionException" );
        }
        catch (NoSuchTransitionException e)
        {
            // expected and correct

            assertEquals( "no transition",
                          e.getId() );
        }
    }

    public void testConnectPlaceToTransition()
        throws Exception
    {
        DefaultNet net = new DefaultNet();

        DefaultPlace place = net.addPlace( "the place" );
        DefaultTransition transition = net.addTransition( "the transition" );

        DefaultArc arc = net.connectPlaceToTransition( "the place",
                                                       "the transition" );

        assertSame( place,
                    arc.getPlace() );

        assertSame( transition,
                    arc.getTransition() );

        assertEquals( 1,
                      place.getArcsToTransitions().length );

        assertSame( arc,
                    place.getArcsToTransitions()[0] );

        assertEquals( 1,
                      transition.getArcsFromPlaces().length );

        assertSame( arc,
                    transition.getArcsFromPlaces()[0] );
    }

    public void testConnectTransitionToPlace()
        throws Exception
    {
        DefaultNet net = new DefaultNet();

        DefaultTransition transition = net.addTransition( "the transition" );
        DefaultPlace place = net.addPlace( "the place" );

        DefaultArc arc = net.connectTransitionToPlace( "the transition",
                                                       "the place" );

        assertSame( transition,
                    arc.getTransition() );

        assertSame( place,
                    arc.getPlace() );

        assertEquals( 1,
                      transition.getArcsToPlaces().length );

        assertSame( arc,
                    transition.getArcsToPlaces()[0] );

        assertEquals( 1,
                      place.getArcsFromTransitions().length );

        assertSame( arc,
                    place.getArcsFromTransitions()[0] );

    }

    public void testCheckUniqueId()
        throws Exception
    {
        DefaultNet net = new DefaultNet();

        net.addPlace( "the id" );

        net.checkUniqueId( "other id" );

        try
        {
            net.checkUniqueId( "the id" );

            fail( "should have thrown DuplicateIdException" );
        }
        catch (DuplicateIdException e)
        {
            // expected and correct
            assertEquals( "the id",
                          e.getId() );
        }
    }
}
