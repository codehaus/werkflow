package com.werken.werkflow.definition;

import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.DefaultNet;

import junit.framework.TestCase;

public class ProcessDefinitionTest
    extends TestCase
{
    public void testConstruct()
    {
        Net net = new DefaultNet();

        ProcessDefinition def = new ProcessDefinition( "the process",
                                                       net,
                                                       MessageType.EMPTY_ARRAY );

        assertEquals( "the process",
                      def.getId() );

        assertSame( net,
                    def.getNet() );

        assertNull( def.getDocumentation() );

        assertNotNull( def.getMessageTypes() );
    }

    public void testDocumentation()
    {
        Net net = new DefaultNet();

        ProcessDefinition def = new ProcessDefinition( "the process",
                                                       net,
                                                       MessageType.EMPTY_ARRAY );

        assertNull( def.getDocumentation() );

        def.setDocumentation( "the docs" );

        assertEquals( "the docs",
                      def.getDocumentation() );
    }
}
