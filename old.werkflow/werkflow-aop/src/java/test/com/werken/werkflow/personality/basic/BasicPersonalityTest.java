package com.werken.werkflow.personality.basic;

import com.werken.werkflow.Wfms;
import com.werken.werkflow.SimpleAttributes;
import com.werken.werkflow.WfmsRuntime;
import com.werken.werkflow.admin.WfmsAdmin;
import com.werken.werkflow.engine.WorkflowEngine;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.ProcessPackage;
import com.werken.werkflow.service.WfmsServices;
import com.werken.werkflow.service.SimpleWfmsServices;
import com.werken.werkflow.service.caserepo.InMemoryCaseRepository;
import com.werken.werkflow.service.messaging.simple.SimpleMessagingManager;

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

        SimpleWfmsServices services = new SimpleWfmsServices();

        services.setCaseRepository( new InMemoryCaseRepository() );

        SimpleMessagingManager msgManager = new SimpleMessagingManager();

        services.setMessagingManager( msgManager );

        Wfms wfms = new WorkflowEngine( services );

        WfmsAdmin admin = wfms.getAdmin();

        ProcessPackage pkg = new ProcessPackage( "foo" );

        pkg.addProcessDefinition( procDefs[0] );

        admin.deployProcessPackage( pkg );

        WfmsRuntime runtime = wfms.getRuntime();

        SimpleAttributes attrs = new SimpleAttributes();

        
        runtime.newProcessCase( "my.proc1",
                                attrs );

        Thread.sleep( 2000 );

        msgManager.acceptMessage( "my message" );
    }
}
