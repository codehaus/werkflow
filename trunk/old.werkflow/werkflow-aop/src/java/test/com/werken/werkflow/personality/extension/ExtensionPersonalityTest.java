package com.werken.werkflow.personality.extension;

import com.werken.werkflow.Wfms;
import com.werken.werkflow.SimpleAttributes;
import com.werken.werkflow.WfmsRuntime;
import com.werken.werkflow.admin.WfmsAdmin;
import com.werken.werkflow.engine.WorkflowEngine;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.personality.Personality;
import com.werken.werkflow.service.WfmsServices;
import com.werken.werkflow.service.SimpleWfmsServices;
import com.werken.werkflow.service.persistence.PersistenceManager;
import com.werken.werkflow.service.persistence.fleeting.FleetingPersistenceManager;
import com.werken.werkflow.service.messaging.simple.SimpleMessagingManager;

import junit.framework.TestCase;

import java.net.URL;

public class ExtensionPersonalityTest
    extends TestCase
{
    public void testLoad()
        throws Exception
    {
        Personality personality = ExtensionPersonality.getInstance();

        assertNotNull( personality );

        URL procUrl = getClass().getResource( "process.xml" );

        assertNull( FooAction.getCheckValue() );

        ProcessDefinition[] procDefs = personality.load( procUrl );

        assertEquals( "bar",
                      FooAction.getCheckValue() );

        assertEquals( 1,
                      procDefs.length );

        assertEquals( "com.werken.werkflow.personality.extension",
                      procDefs[0].getPackageId() );

        assertEquals( "test",
                      procDefs[0].getId() );

    }
}
