package com.werken.werkflow;

import com.werken.werkflow.WfmsRuntime;
import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.SimpleAttributes;
import com.werken.werkflow.admin.WfmsAdmin;
import com.werken.werkflow.engine.WorkflowEngine;
import com.werken.werkflow.service.WfmsServices;
import com.werken.werkflow.service.SimpleWfmsServices;
import com.werken.werkflow.service.messaging.simple.SimpleMessagingManager;
import com.werken.werkflow.service.persistence.PersistenceManager;
import com.werken.werkflow.service.persistence.fleeting.FleetingPersistenceManager;
import com.werken.werkflow.syntax.fundamental.FundamentalDefinitionLoader;
import com.werken.werkflow.definition.ProcessDefinition;

import java.net.URL;

public class GeneralTest
    extends WerkflowTestCase
{
    public void test1()
        throws Exception
    {
        SimpleWfmsServices services = new SimpleWfmsServices();

        SimpleMessagingManager messagingManager = new SimpleMessagingManager();
        PersistenceManager persistenceManager = new FleetingPersistenceManager();

        services.setMessagingManager( messagingManager );
        services.setPersistenceManager( persistenceManager );

        WorkflowEngine engine = new WorkflowEngine( services );

        URL url = getClass().getResource( "general2.xml" );

        FundamentalDefinitionLoader loader = new FundamentalDefinitionLoader();

        ProcessDefinition[] processDefs = loader.load( url );

        WfmsAdmin admin = engine.getAdmin();

        assertLength( 1,
                      processDefs );

        for ( int i = 0 ; i < processDefs.length ; ++i )
        {
            // System.err.println( processDefs[i] );
            admin.deployProcess( processDefs[i] );
        }

        WfmsRuntime runtime = engine.getRuntime();

        SimpleAttributes attrs = new SimpleAttributes();

        ProcessCase processCase = runtime.callProcess( "",
                                                       "general",
                                                       attrs );

        assertNotNull( processCase );
    }
}
