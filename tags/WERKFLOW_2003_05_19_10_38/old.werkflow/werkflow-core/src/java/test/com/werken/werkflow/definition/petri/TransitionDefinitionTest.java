package com.werken.werkflow.definition.petri;

import junit.framework.TestCase;

public class TransitionDefinitionTest
    extends TestCase
{
    public void testConstruct()
    {
        TransitionDefinition transition = new TransitionDefinition( "my.id",
                                                                    "my.docs" );

        assertEquals( "my.id",
                      transition.getId() );

        assertEquals( "my.docs",
                      transition.getDocumentation() );
    }

    public void testEquals()
    {
        TransitionDefinition transition = new TransitionDefinition( "my.transition",
                                                                    "my.docs" );

        TransitionDefinition testTransition = null;

        testTransition = new TransitionDefinition( "my.transition",
                                                   "my.docs" );

        assertTrue( transition.equals( testTransition ) );
        assertTrue( testTransition.equals( transition ) );

        testTransition = new TransitionDefinition( "my.transition",
                                                   "my.other.docs" );

        assertTrue( transition.equals( testTransition ) );
        assertTrue( testTransition.equals( transition ) );

        testTransition = new TransitionDefinition( "my.other.transition",
                                                   "my.docs" );

        assertFalse( transition.equals( testTransition ) );
        assertFalse( testTransition.equals( transition ) );
    }
}
