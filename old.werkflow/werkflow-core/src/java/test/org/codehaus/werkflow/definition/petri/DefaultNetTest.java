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
