package com.werken.werkflow.definition.petri;

import junit.framework.TestCase;

public class DefaultNodeTest
    extends TestCase
{
    public void testConstruct()
    {
        DefaultNode node = new DefaultNode( "the id" );

        assertEquals( "the id",
                      node.getId() );

        assertNull( node.getDocumentation() );

        assertEquals( 0,
                      node.getInboundArcs().length );

        assertEquals( 0,
                      node.getOutboundArcs().length );
    }
}
