package com.werken.werkflow;

import com.werken.werkflow.admin.WfmsAdmin;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.fundamental.FundamentalDefinitionLoader;
import com.werken.werkflow.engine.WorkflowEngine;
import com.werken.werkflow.service.WfmsServices;
import com.werken.werkflow.service.SimpleWfmsServices;
import com.werken.werkflow.service.caserepo.InMemoryCaseRepository;
import com.werken.werkflow.service.messaging.simple.SimpleMessagingManager;

import junit.framework.TestCase;

import java.net.URL;

public class GeneralTest
    extends TestCase
{
    public void testOne()
        throws Exception
    {
        SimpleWfmsServices services = new SimpleWfmsServices();

        SimpleMessagingManager messagingManager = new SimpleMessagingManager();

        services.setCaseRepository( new InMemoryCaseRepository() );
        services.setMessagingManager( messagingManager );

        Wfms wfms = new WorkflowEngine( services );

        FundamentalDefinitionLoader loader = new FundamentalDefinitionLoader();

        URL url = getClass().getResource( "general.xml" );

        ProcessDefinition[] defs = loader.load( url );

        WfmsAdmin admin = wfms.getAdmin();

        for ( int i = 0 ; i < defs.length ; ++i )
        {
            admin.deployProcess( defs[i] );
        }

        SimpleAttributes attrs = new SimpleAttributes();

        attrs.setAttribute( "bob",
                            "mcwhirter" );

        WfmsRuntime runtime = wfms.getRuntime();

        ProcessCase processCase = runtime.newProcessCase( "general",
                                                          attrs );

        System.err.println( "sleep #1" );

        Thread.sleep( 1000 );

        System.err.println( "sending msg 'mcwhirter'" );

        messagingManager.acceptMessage( new SimpleMessage( "normal",
                                                           "mcwhirter" ) );

        System.err.println( "sleep #2" );

        Thread.sleep( 1000 );

        System.err.println( "sending msg 'mcstrachan'" );

        messagingManager.acceptMessage( new SimpleMessage( "normal",
                                                           "mcstrachan" ) );


        System.err.println( "sleep #3" );

        Thread.sleep( 1000 );

        SimpleAttributes attrsToo = new SimpleAttributes();

        attrsToo.setAttribute( "bob",
                               "cheese" );

        ProcessCase caseToo = runtime.newProcessCase( "general",
                                                      attrsToo ); 

        System.err.println( "sleep #4" );

        Thread.sleep( 1000 );
        
        System.err.println( "sending msg 'mcstrachan'" );

        messagingManager.acceptMessage( new SimpleMessage( "normal",
                                                           "mcstrachan" ) );

        System.err.println( "sleep #5" );

        Thread.sleep( 1000 );

        messagingManager.acceptMessage( new SimpleMessage( "init",
                                                           "larry" ) );

        Thread.sleep( 1000 );
    }
}
