package com.werken.werkflow.initiator.message;

import com.werken.werkflow.WerkflowTestCase;
import com.werken.werkflow.admin.WfmsAdmin;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.engine.WorkflowEngine;
import com.werken.werkflow.personality.basic.BasicPersonality;
import com.werken.werkflow.service.SimpleWfmsServices;
import com.werken.werkflow.service.messaging.simple.SimpleMessagingManager;
import com.werken.werkflow.service.persistence.PersistenceManager;
import com.werken.werkflow.service.persistence.fleeting.FleetingPersistenceManager;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ActivityManagerTest
    extends WerkflowTestCase
{
    /** Workflow services. */
    private SimpleWfmsServices services;
    /** Workflow Persistence Manager. */
    private PersistenceManager persistenceManager;
    /** Workflow messaging manager. */
    private SimpleMessagingManager messagingManager;
    /** Workflow engine. */
    private WorkflowEngine engine;

    public void setUp()
        throws Exception
    {
        services = new SimpleWfmsServices();
        messagingManager = new SimpleMessagingManager();
        persistenceManager = new FleetingPersistenceManager();
        services.setMessagingManager( messagingManager );
        services.setPersistenceManager( persistenceManager );
        engine = new WorkflowEngine( services );
    }

    public void testMessage_Initiated_Flow_With_Entity_Sent_Via_MessagingManager()
        throws Exception
    {

        URL url = getClass().getResource( "workflow.xml" );

        Map beans = new HashMap();
        beans.put( "actionManager", new ActionManager() );

        BasicPersonality bp = BasicPersonality.getInstance();

        ProcessDefinition[] processDefs = bp.load( url, beans );
        WfmsAdmin admin = engine.getAdmin();

        for ( int i = 0 ; i < processDefs.length ; ++i )
        {
            admin.deployProcess( processDefs[i] );
            System.out.println( "processDefs = " + processDefs[i] );
        }

        Entity entity = new Entity();
        messagingManager.acceptMessage( entity );

        //assertTrue( "The entity has not been touched!", entity.hasBeenTouched() );
    }
}
