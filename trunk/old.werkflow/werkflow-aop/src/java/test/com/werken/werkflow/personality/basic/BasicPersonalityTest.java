package com.werken.werkflow.personality.basic;

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

public class BasicPersonalityTest
    extends TestCase
{
    public void testLoad()
        throws Exception
    {
        Personality personality = BasicPersonality.getInstance();

        assertNotNull( personality );

        URL procUrl = getClass().getResource( "process1.xml" );

        ProcessDefinition[] procDefs = personality.load( procUrl );

        assertEquals( 1,
                      procDefs.length );

        assertEquals( "my.pkg",
                      procDefs[0].getPackageId() );

        assertEquals( "my.proc1",
                      procDefs[0].getId() );

        SimpleWfmsServices services = new SimpleWfmsServices();

        services.setPersistenceManager( new FleetingPersistenceManager() );

        SimpleMessagingManager msgManager = new SimpleMessagingManager();

        services.setMessagingManager( msgManager );

        Wfms wfms = new WorkflowEngine( services );

        WfmsAdmin admin = wfms.getAdmin();

        admin.deployProcess( procDefs[0] );

        WfmsRuntime runtime = wfms.getRuntime();

        SimpleAttributes attrs = new SimpleAttributes();

        
        /*
        runtime.newProcessCase( "my.proc1",
                                attrs );
        */

        // Thread.sleep( 2000 );

        msgManager.acceptMessage( "strachan" );
        msgManager.acceptMessage( "mcwhirter" );

        msgManager.acceptMessage( new Integer( 10 ) );
        msgManager.acceptMessage( new Integer( 9 ) );
        msgManager.acceptMessage( new Integer( 8 ) );

        Thread.sleep( 2000 );
    }
}
