package com.werken.werkflow.core;

import com.werken.werkflow.WerkflowTestCase;
import com.werken.werkflow.Attributes;
import com.werken.werkflow.definition.petri.DefaultNet;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.service.messaging.DefaultMessage;
import com.werken.werkflow.service.persistence.ProcessPersistenceManager;
import com.werken.werkflow.service.persistence.fleeting.FleetingProcessPersistenceManager;

public class CoreWorkItemTest
    extends WerkflowTestCase
{
    public void testConstruct()
        throws Exception
    {
        ProcessPersistenceManager persistManager = new FleetingProcessPersistenceManager( "the.pkg",
                                                                                          "the.process" );

        CoreProcessCase processCase = new CoreProcessCase( persistManager,
                                                           null,
                                                           null,
                                                           null,
                                                           null,
                                                           persistManager.newCase( Attributes.EMPTY_ATTRIBUTES ) );

        DefaultNet net = new DefaultNet();

        DefaultTransition transition = net.addTransition( "the.transition" );

        String[] tokens = new String[]
            {
                "place.one",
                "place.two",
                "place.three",
            };

        CoreWorkItem workItem = new CoreWorkItem( processCase,
                                                  transition,
                                                  tokens,
                                                  "the.message" );

        assertSame( processCase,
                    workItem.getCase() );

        assertSame( transition,
                    workItem.getTransition() );

        assertSame( tokens,
                    workItem.getTokens() );

        assertSame( "the.message",
                    workItem.getMessageId() );

        workItem = new CoreWorkItem( processCase,
                                     transition,
                                     tokens );

        assertSame( processCase,
                    workItem.getCase() );

        assertSame( transition,
                    workItem.getTransition() );

        assertSame( tokens,
                    workItem.getTokens() );

        assertNull( workItem.getMessageId() );
    }

    public void testSatisfy()
        throws Exception
    {
        MockMessageConsumer messageConsumer = new MockMessageConsumer();

        messageConsumer.addMessage( new DefaultMessage( "the.message",
                                                        null,
                                                        new Object() ) );

        ProcessPersistenceManager persistManager = new FleetingProcessPersistenceManager( "the.pkg",
                                                                                          "the.process" );

        CoreProcessCase processCase = new CoreProcessCase( persistManager,
                                                           null,
                                                           null,
                                                           messageConsumer,
                                                           null,
                                                           persistManager.newCase( Attributes.EMPTY_ATTRIBUTES ) );

        processCase.addToken( "place.one" );
        processCase.addToken( "place.two" );
        processCase.addToken( "place.three" );
        processCase.addToken( "place.four" );

        DefaultNet net = new DefaultNet();

        DefaultTransition transition = net.addTransition( "the.transition" );

        String[] tokens = new String[]
            {
                "place.one",
                "place.two",
                "place.three",
            };

        CoreWorkItem workItem = new CoreWorkItem( processCase,
                                                  transition,
                                                  tokens,
                                                  "the.message" );

        CoreChangeSet changeSet = new CoreChangeSet( null );

        CoreActivity activity = workItem.satisfy( changeSet );

        assertNotNull( activity );

        assertLength( 1,
                      processCase.getTokens() );

        assertContains( "place.four",
                        processCase.getTokens() );

        assertContains( processCase,
                        changeSet.getCoreModifiedCases() );

        assertContains( "the.message",
                        changeSet.getCoreConsumptions() );
    }

    public void testSatisfy_notSatisfiableTokens()
        throws Exception
    {
        MockMessageConsumer messageConsumer = new MockMessageConsumer();

        messageConsumer.addMessage( new DefaultMessage( "the.message",
                                                        null,
                                                        new Object() ) );

        ProcessPersistenceManager persistManager = new FleetingProcessPersistenceManager( "the.pkg",
                                                                                          "the.process" );

        CoreProcessCase processCase = new CoreProcessCase( persistManager,
                                                           null,
                                                           null,
                                                           messageConsumer,
                                                           null,
                                                           persistManager.newCase( Attributes.EMPTY_ATTRIBUTES ) );

        processCase.addToken( "place.one" );
        processCase.addToken( "place.two" );

        DefaultNet net = new DefaultNet();

        DefaultTransition transition = net.addTransition( "the.transition" );

        String[] tokens = new String[]
            {
                "place.one",
                "place.two",
                "place.three",
            };

        CoreWorkItem workItem = new CoreWorkItem( processCase,
                                                  transition,
                                                  tokens,
                                                  "the.message" );

        CoreChangeSet changeSet = new CoreChangeSet( null );

        CoreActivity activity = workItem.satisfy( changeSet );

        assertNull( activity );

        assertLength( 2,
                      processCase.getTokens() );

        assertContains( "place.one",
                        processCase.getTokens() );

        assertContains( "place.two",
                        processCase.getTokens() );

        assertEmpty( changeSet.getCoreModifiedCases() );
        assertEmpty( changeSet.getCoreConsumptions() );
    }

    public void testSatisfy_notSatisfiableMessage()
        throws Exception
    {
        MockMessageConsumer messageConsumer = new MockMessageConsumer();

        ProcessPersistenceManager persistManager = new FleetingProcessPersistenceManager( "the.pkg",
                                                                                          "the.process" );

        CoreProcessCase processCase = new CoreProcessCase( persistManager,
                                                           null,
                                                           null,
                                                           messageConsumer,
                                                           null,
                                                           persistManager.newCase( Attributes.EMPTY_ATTRIBUTES ) );

        processCase.addToken( "place.one" );
        processCase.addToken( "place.two" );
        processCase.addToken( "place.three" );
        processCase.addToken( "place.four" );

        DefaultNet net = new DefaultNet();

        DefaultTransition transition = net.addTransition( "the.transition" );

        String[] tokens = new String[]
            {
                "place.one",
                "place.two",
                "place.three",
            };

        CoreWorkItem workItem = new CoreWorkItem( processCase,
                                                  transition,
                                                  tokens,
                                                  "the.message" );

        CoreChangeSet changeSet = new CoreChangeSet( null );

        CoreActivity activity = workItem.satisfy( changeSet );

        assertNull( activity );

        assertLength( 4,
                      processCase.getTokens() );

        assertContains( "place.one",
                        processCase.getTokens() );

        assertContains( "place.two",
                        processCase.getTokens() );

        assertContains( "place.three",
                        processCase.getTokens() );

        assertContains( "place.four",
                        processCase.getTokens() );

        assertEmpty( changeSet.getCoreModifiedCases() );
        assertEmpty( changeSet.getCoreConsumptions() );
    }

    public void testSatisfy_noMessage()
        throws Exception
    {
        MockMessageConsumer messageConsumer = new MockMessageConsumer();

        ProcessPersistenceManager persistManager = new FleetingProcessPersistenceManager( "the.pkg",
                                                                                          "the.process" );

        CoreProcessCase processCase = new CoreProcessCase( persistManager,
                                                           null,
                                                           null,
                                                           messageConsumer,
                                                           null,
                                                           persistManager.newCase( Attributes.EMPTY_ATTRIBUTES ) );

        processCase.addToken( "place.one" );
        processCase.addToken( "place.two" );
        processCase.addToken( "place.three" );
        processCase.addToken( "place.four" );

        DefaultNet net = new DefaultNet();

        DefaultTransition transition = net.addTransition( "the.transition" );

        String[] tokens = new String[]
            {
                "place.one",
                "place.two",
                "place.three",
            };

        CoreWorkItem workItem = new CoreWorkItem( processCase,
                                                  transition,
                                                  tokens,
                                                  null );

        CoreChangeSet changeSet = new CoreChangeSet( null );

        CoreActivity activity = workItem.satisfy( changeSet );

        assertNotNull( activity );

        assertLength( 1,
                      processCase.getTokens() );

        assertContains( "place.four",
                        processCase.getTokens() );

        assertContains( processCase,
                        changeSet.getCoreModifiedCases() );

        assertEmpty( changeSet.getCoreConsumptions() );
    }
}
