package com.werken.werkflow.core;

import com.werken.werkflow.WerkflowTestCase;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.service.messaging.MessagingManager;
import com.werken.werkflow.service.messaging.simple.SimpleMessagingManager;
import com.werken.werkflow.service.persistence.PersistenceManager;
import com.werken.werkflow.service.persistence.fleeting.FleetingPersistenceManager;

public class DeploymentManagerTest
    extends WerkflowTestCase
{
    public void testDeployProcess()
        throws Exception
    {
        Executor           executor         = new Executor();
        PersistenceManager persistManager   = new FleetingPersistenceManager();
        MessagingManager   messagingManager = new SimpleMessagingManager();

        DeploymentManager deploymentManager = new DeploymentManager( executor,
                                                                     persistManager,
                                                                     messagingManager );

        /*
        ProcessDefinition processDef = new ProcessDefinition( "the.package",
                                                              "the.process",
                                                              ProcessDefinition.InitiationType.MESSAGE );

        deploymentManager.deployProcess( processDef );

        ProcessDeployment deployment = deploymentManager.getDeployment( "the.package",
                                                                        "the.process" );
        assertNotNull( deployment );
        */
    }
}
