package com.werken.werkflow.personality.basic;

import com.werken.werkflow.definition.ProcessDefinition;

import junit.framework.TestCase;

import java.net.URL;

public class BasicPersonalityTest
    extends TestCase
{
    public void testLoad()
        throws Exception
    {
        BasicPersonality personality = BasicPersonality.getInstance();

        assertNotNull( personality );

        URL procUrl = getClass().getResource( "process1.xml" );

        ProcessDefinition[] procDefs = personality.load( procUrl );

        assertEquals( 1,
                      procDefs.length );

        assertEquals( "my.proc1",
                      procDefs[0].getId() );
    }
}
