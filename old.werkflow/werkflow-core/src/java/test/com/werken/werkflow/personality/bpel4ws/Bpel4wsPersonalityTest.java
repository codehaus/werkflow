package com.werken.werkflow.personality.bpel4ws;

import com.werken.werkflow.definition.ProcessDefinition;

import junit.framework.TestCase;

import java.net.URL;

public class Bpel4wsPersonalityTest
    extends TestCase
{
    public void testLoad()
        throws Exception
    {
        Bpel4wsPersonality personality = Bpel4wsPersonality.getInstance();

        assertNotNull( personality );

        URL procUrl = getClass().getResource( "process1.xml" );

        ProcessDefinition[] procDefs = personality.load( procUrl );

        assertEquals( 1,
                      procDefs.length );

        assertEquals( "http://myco.com/#my.proc1",
                      procDefs[0].getId() );
    }
}
