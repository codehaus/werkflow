package com.werken.werkflow.definition.petri;

import junit.framework.TestCase;

public class PlaceDefinitionTest
    extends TestCase
{
    public void testConstruct()
    {
        PlaceDefinition place = new PlaceDefinition( "my.id",
                                                     "my.docs" );

        assertEquals( "my.id",
                      place.getId() );

        assertEquals( "my.docs",
                      place.getDocumentation() );

        assertNull( place.getStashId() );
    }

    public void testStash()
    {
        PlaceDefinition place = new PlaceDefinition( "my.id",
                                                     "my.docs" );

        assertNull( place.getStashId() );

        place.setStashId( "my.stash" );

        assertEquals( "my.stash",
                      place.getStashId() );
    }

    public void testEquals()
    {
        PlaceDefinition place = new PlaceDefinition( "my.place",
                                                     "my.docs" );

        PlaceDefinition testPlace = null;

        testPlace = new PlaceDefinition( "my.place",
                                         "my.docs" );

        assertTrue( place.equals( testPlace ) );
        assertTrue( testPlace.equals( place ) );

        testPlace = new PlaceDefinition( "my.place",
                                         "my.other.docs" );

        assertTrue( place.equals( testPlace ) );
        assertTrue( testPlace.equals( place ) );

        testPlace = new PlaceDefinition( "my.other.place",
                                         "my.docs" );

        assertFalse( place.equals( testPlace ) );
        assertFalse( testPlace.equals( place ) );
    }
}
