package com.werken.werkflow;

import com.werken.werkflow.admin.WfmsAdmin;
import com.werken.werkflow.definition.ProcessPackage;
import com.werken.werkflow.engine.WorkflowEngine;
import com.werken.werkflow.service.WfmsServices;
import com.werken.werkflow.service.SimpleWfmsServices;
import com.werken.werkflow.service.caserepo.InMemoryCaseRepository;
import com.werken.werkflow.service.messaging.simple.SimpleMessagingManager;
import com.werken.werkflow.syntax.fundamental.FundamentalDefinitionLoader;

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

        ProcessPackage pkg = loader.load( url );

        WfmsAdmin admin = wfms.getAdmin();

        admin.deployProcessPackage( pkg );

        messagingManager.acceptMessage( new SimpleMessage( "init",
                                                           "larry" ) );

        messagingManager.acceptMessage( new SimpleMessage( "normal",
                                                           "mcstrachan" ) );

        messagingManager.acceptMessage( new SimpleMessage( "normal",
                                                           "mr mcstrachan" ) );

        messagingManager.acceptMessage( new SimpleMessage( "normal",
                                                           "larry" ) );

        messagingManager.acceptMessage( new SimpleMessage( "init",
                                                           "mcstrachan" ) );

        messagingManager.acceptMessage( new SimpleMessage( "normal",
                                                           "mr larry" ) );

        Thread.sleep( 1000 );
    }
}
