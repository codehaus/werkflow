package com.werken.werkflow.definition.petri;

import junit.framework.TestCase;

public class ArcDefinitionTest
    extends TestCase
{
    public void testConstruct()
    {
        ArcDefinition arc = new ArcDefinition( ArcDefinition.PLACE_TO_TRANSITION_TYPE,
                                               "my.place",
                                               "my.transition",
                                               "my.expression" );

        assertEquals( ArcDefinition.PLACE_TO_TRANSITION_TYPE,
                      arc.getType() );

        assertEquals( "my.place",
                      arc.getPlaceId() );

        assertEquals( "my.transition",
                      arc.getTransitionId() );

        assertEquals( "my.expression",
                      arc.getExpression() );
    }

    public void testEquals()
    {
        ArcDefinition arc = new ArcDefinition( ArcDefinition.PLACE_TO_TRANSITION_TYPE,
                                               "my.place",
                                               "my.transition",
                                                  "my.expression" );

        ArcDefinition testArc = null;

        testArc = new ArcDefinition( ArcDefinition.PLACE_TO_TRANSITION_TYPE,
                                     "my.place",
                                     "my.transition",
                                     "my.expression" );


        assertTrue( arc.equals( testArc ) );
        assertTrue( testArc.equals( arc ) );

        testArc = new ArcDefinition( ArcDefinition.TRANSITION_TO_PLACE_TYPE,
                                     "my.place",
                                     "my.transition",
                                     "my.expression" );

        assertFalse( arc.equals( testArc ) );
        assertFalse( testArc.equals( arc ) );

        testArc = new ArcDefinition( ArcDefinition.PLACE_TO_TRANSITION_TYPE,
                                     "my.place",
                                     "my.other.transition",
                                     "my.expression" );

        assertFalse( arc.equals( testArc ) );
        assertFalse( testArc.equals( arc ) );

        testArc = new ArcDefinition( ArcDefinition.PLACE_TO_TRANSITION_TYPE,
                                     "my.other.place",
                                     "my.transition",
                                     "my.expression" );

        assertFalse( arc.equals( testArc ) );
        assertFalse( testArc.equals( arc ) );

    }
}
