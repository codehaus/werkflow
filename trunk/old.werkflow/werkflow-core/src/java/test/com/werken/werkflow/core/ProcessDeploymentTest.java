package com.werken.werkflow.core;

import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.service.messaging.MessagingManager;
import com.werken.werkflow.service.messaging.simple.SimpleMessagingManager;
import com.werken.werkflow.service.persistence.PersistenceManager;
import com.werken.werkflow.service.persistence.PersistenceException;
import com.werken.werkflow.service.persistence.fleeting.FleetingPersistenceManager;

import com.werken.werkflow.WerkflowTestCase;

public class ProcessDeploymentTest
    extends WerkflowTestCase
{
    public void testConstruct()
        throws Exception
    {
        Executor           executor         = new Executor();
        Scheduler          scheduler        = new Scheduler( executor );
        PersistenceManager persistManager   = new FleetingPersistenceManager();
        MessagingManager   messagingManager = new SimpleMessagingManager();

        ProcessDefinition processDef = new ProcessDefinition( "the.package",
                                                              "the.process",
                                                              ProcessDefinition.InitiationType.CALL );

        processDef.setDocumentation( "the docs" );

        ProcessDeployment deployment = new ProcessDeployment( processDef,
                                                              scheduler,
                                                              persistManager.deployProcess( processDef ),
                                                              messagingManager );

        assertEquals( "the.package",
                      deployment.getPackageId() );

        assertEquals( "the.process",
                      deployment.getId() );

        assertEquals( "the docs",
                      deployment.getDocumentation() );

        assertSame( scheduler,
                    deployment.getScheduler() );

        assertSame( processDef,
                    deployment.getProcessDefinition() );

        assertNotNull( deployment.getEvaluator() );

        assertNotNull( deployment.getMessageHandler() );
    }

    public void testNewTransaction()
        throws Exception
    {
        Executor           executor         = new Executor();
        Scheduler          scheduler        = new Scheduler( executor );
        PersistenceManager persistManager   = new FleetingPersistenceManager();
        MessagingManager   messagingManager = new SimpleMessagingManager();

        ProcessDefinition processDef = new ProcessDefinition( "the.package",
                                                              "the.process",
                                                              ProcessDefinition.InitiationType.CALL );

        final Relay commits = new Relay();

        ProcessDeployment deployment = new ProcessDeployment( processDef,
                                                              scheduler,
                                                              persistManager.deployProcess( processDef ),
                                                              messagingManager )
            {
                public void commit(CoreChangeSet changeSet)
                    throws PersistenceException
                {
                    commits.setValue( Boolean.TRUE );
                    super.commit( changeSet );
                }
            };

        CoreChangeSet changeSet = deployment.newChangeSet();
        
        assertNotNull( changeSet );
        
        assertNull( commits.getValue() );

        changeSet.commit();

        assertNotNull( commits.getValue() );
    }
}
