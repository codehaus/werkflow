package com.werken.werkflow.definition.petri;

import junit.framework.TestCase;

public class DefaultElementTest
    extends TestCase
{
    public void testConstruct()
    {
        DefaultElement elem = new DefaultElement();

        assertNull( elem.getDocumentation() );
    }

    public void testDocumentation()
    {
        DefaultElement node = new DefaultElement();

        assertNull( node.getDocumentation() );

        node.setDocumentation( "the docs" );

        assertEquals( "the docs",
                      node.getDocumentation() );
    }
}
