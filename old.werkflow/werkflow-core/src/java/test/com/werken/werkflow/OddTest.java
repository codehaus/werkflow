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

public class OddTest
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

        URL url = getClass().getResource( "odd.xml" );

        ProcessDefinition[] defs = loader.load( url );

        WfmsAdmin admin = wfms.getAdmin();

        for ( int i = 0 ; i < defs.length ; ++i )
        {
            admin.deployProcess( defs[i] );
        }

        messagingManager.acceptMessage( new Integer( 42 ) );

        Thread.sleep( 1000 );

        messagingManager.acceptMessage( new Integer( 4200 ) );

        Thread.sleep( 1000 );
    }
}
