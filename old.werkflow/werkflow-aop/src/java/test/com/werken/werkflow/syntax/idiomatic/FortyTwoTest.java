package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.Wfms;

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

public class FortyTwoTest
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

        IdiomaticDefinitionLoader loader = new IdiomaticDefinitionLoader();

        URL url = getClass().getResource( "fortytwo.xml" );

        ProcessPackage pkg = loader.load( url );

        WfmsAdmin admin = wfms.getAdmin();

        admin.deployProcessPackage( pkg );

        messagingManager.acceptMessage( new Integer( 42 ) );

        Thread.sleep( 1000 );

        messagingManager.acceptMessage( new Integer( 4200 ) );

        Thread.sleep( 1000 );
    }
}
