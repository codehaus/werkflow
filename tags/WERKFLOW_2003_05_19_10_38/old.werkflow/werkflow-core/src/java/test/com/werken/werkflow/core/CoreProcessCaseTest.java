package com.werken.werkflow.core;

import com.werken.werkflow.Attributes;
import com.werken.werkflow.MockProcessInfo;
import com.werken.werkflow.WerkflowTestCase;
import com.werken.werkflow.service.persistence.ProcessPersistenceManager;
import com.werken.werkflow.service.persistence.fleeting.FleetingProcessPersistenceManager;

public class CoreProcessCaseTest
    extends WerkflowTestCase
{
    public void testConstruct()
        throws Exception
    {
        ProcessPersistenceManager persistManager = new FleetingProcessPersistenceManager( "the.pkg",
                                                                                          "the.process" );
        MockChangeSetSource   changeSetSource = new MockChangeSetSource();
        MockCaseEvaluator     caseEvaluator   = new MockCaseEvaluator();
        MockMessageConsumer   messageConsumer = new MockMessageConsumer();
        MockProcessInfo       processInfo     = new MockProcessInfo( "the.pkg",
                                                                     "the.process",
                                                                     "the docs" );
        
        CoreProcessCase processCase = new CoreProcessCase( persistManager,
                                                           changeSetSource,
                                                           caseEvaluator,
                                                           messageConsumer,
                                                           processInfo,
                                                           persistManager.newCase( Attributes.EMPTY_ATTRIBUTES ) );

        assertSame( changeSetSource,
                    processCase.getChangeSetSource() );

        assertSame( caseEvaluator,
                    processCase.getCaseEvaluator() );

        assertSame( messageConsumer,
                    processCase.getMessageConsumer() );

        assertSame( processInfo,
                    processCase.getProcessInfo() );

        assertEquals( "case.1",
                      processCase.getId() );

        assertEmpty( processCase.getAttributeNames() );

        assertEmpty( processCase.getTokens() );

        assertEmpty( processCase.getScheduledActivities() );

        assertEmpty( processCase.getCorrelations() );
    }

    public void testAttributes()
        throws Exception
    {
        CoreProcessCase processCase = newCase();

        assertEmpty( processCase.getAttributeNames() );

        Object attr1 = new Object();

        processCase.setAttribute( "attr1",
                                  attr1 );

        assertTrue( processCase.hasAttribute( "attr1" ) );

        assertSame( attr1,
                    processCase.getAttribute( "attr1" ) );

        assertLength( 1,
                      processCase.getAttributeNames() );

        assertContains( "attr1",
                        processCase.getAttributeNames() );

        Object attr1b = new Object();

        processCase.setAttribute( "attr1",
                                  attr1b );

        assertTrue( processCase.hasAttribute( "attr1" ) );

        assertSame( attr1b,
                    processCase.getAttribute( "attr1" ) );

        assertLength( 1,
                      processCase.getAttributeNames() );

        assertContains( "attr1",
                        processCase.getAttributeNames() );

        Object attr2 = new Object();

        processCase.setAttribute( "attr2",
                                  attr2 );

        assertTrue( processCase.hasAttribute( "attr2" ) );

        assertSame( attr2,
                    processCase.getAttribute( "attr2" ) );

        assertLength( 2,
                      processCase.getAttributeNames() );

        assertContains( "attr1",
                        processCase.getAttributeNames() );

        assertContains( "attr2",
                        processCase.getAttributeNames() );

        assertTrue( processCase.hasAttribute( "attr1" ) );
    }

    public void testTokens()
        throws Exception
    {
        CoreProcessCase processCase = newCase();

        assertLength( 0,
                      processCase.getTokens() );

        processCase.addToken( "token.1" );
        processCase.addToken( "token.2" );
        processCase.addToken( "token.3" );

        assertLength( 3,
                      processCase.getTokens() );

        assertContains( new String[] {
            "token.1",
            "token.2",
            "token.3",
        },
                        processCase.getTokens() );

        assertTrue( processCase.hasTokens( new String[]
            {
                "token.1",
                "token.2",
                "token.3",
            } ) );

        assertFalse( processCase.hasTokens( new String[]
            {
                "token.1",
                "token.99",
            } ) );

        processCase.consumeTokens( new String[]
            {
                "token.1",
                "token.3",
            } );

        assertLength( 1,
                      processCase.getTokens() );

        assertContains( "token.2",
                        processCase.getTokens() );
    }

    public void testCorrelationsConsistency()
        throws Exception
    {
        CoreProcessCase processCase = newCase();

        Correlation bogus = new Correlation( "other.case",
                                             "trans.1",
                                             "message.1" );

        try
        {
            processCase.addCorrelation( bogus );
        }
        catch (ConsistencyException e)
        {
            // expected and correct
        }
    }

    public void testCorrelations()
        throws Exception
    {
        CoreProcessCase processCase = newCase();

        assertEmpty( processCase.getCorrelations() );

        Correlation cor1 = new Correlation( processCase.getId(),
                                            "trans.1",
                                            "message.1" );

        Correlation cor2 = new Correlation( processCase.getId(),
                                            "trans.1",
                                            "message.2" );

        Correlation cor3 = new Correlation( processCase.getId(),
                                            "trans.2",
                                            "message.3" );

        processCase.addCorrelation( cor1 );
        processCase.addCorrelation( cor2 );
        processCase.addCorrelation( cor3 );

        assertLength( 3,
                      processCase.getCorrelations() );

        assertContains( new Correlation[]
            {
                cor1,
                cor2,
                cor3,
            },
                        processCase.getCorrelations() );

        assertLength( 2,
                      processCase.getCorrelations( "trans.1" ) );

        assertContains( new Correlation[]
            {
                cor1,
                cor2,
            },
                      processCase.getCorrelations( "trans.1" ) );

        assertLength( 1,
                      processCase.getCorrelations( "trans.2" ) );

        assertContains( new Correlation[]
            {
                cor3,
            },
                       processCase.getCorrelations( "trans.2" ) );
        

        processCase.removeCorrelationsByMessage( "message.1" );

        assertLength( 2,
                      processCase.getCorrelations() );

        assertContains( new Correlation[]
            {
                cor2,
                cor3,
            },
                        processCase.getCorrelations() );

        processCase.removeCorrelationsByTransition( "trans.2" );

        assertLength( 1,
                      processCase.getCorrelations() );

        assertContains( new Correlation[]
            {
                cor2,
            },
                        processCase.getCorrelations() );
    }

    CoreProcessCase newCase()
        throws Exception
    {
        ProcessPersistenceManager persistManager = new FleetingProcessPersistenceManager( "the.pkg",
                                                                                          "the.process" );
        MockChangeSetSource   changeSetSource = new MockChangeSetSource();
        MockCaseEvaluator     caseEvaluator   = new MockCaseEvaluator();
        MockMessageConsumer   messageConsumer = new MockMessageConsumer();
        MockProcessInfo       processInfo     = new MockProcessInfo( "the.pkg",
                                                                     "the.process",
                                                                     "the docs" );
        
        return new CoreProcessCase( persistManager,
                                    changeSetSource,
                                    caseEvaluator,
                                    messageConsumer,
                                    processInfo,
                                    persistManager.newCase( Attributes.EMPTY_ATTRIBUTES ) );
    }
}
