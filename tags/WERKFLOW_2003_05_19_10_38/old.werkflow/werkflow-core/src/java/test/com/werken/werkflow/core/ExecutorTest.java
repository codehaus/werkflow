package com.werken.werkflow.core;

import com.werken.werkflow.WerkflowTestCase;
import com.werken.werkflow.Attributes;
import com.werken.werkflow.SimpleAttributes;
import com.werken.werkflow.MockProcessInfo;
import com.werken.werkflow.definition.petri.DefaultNet;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.service.persistence.ProcessPersistenceManager;
import com.werken.werkflow.service.persistence.fleeting.FleetingProcessPersistenceManager;
import com.werken.werkflow.task.DefaultTask;
import com.werken.werkflow.work.Action;
import com.werken.werkflow.work.ActionInvocation;

public class ExecutorTest
    extends WerkflowTestCase
{
    public void testTransferAttributes()
        throws Exception
    {
        ProcessPersistenceManager persistManager = new FleetingProcessPersistenceManager( "the.pkg",
                                                                                          "the.process" );

        CoreProcessCase processCase = new CoreProcessCase( null,
                                                           null,
                                                           null,
                                                           null,
                                                           null,
                                                           persistManager.newCase( Attributes.EMPTY_ATTRIBUTES ) );

        Executor executor = new Executor();

        SimpleAttributes attrs = new SimpleAttributes();

        attrs.setAttribute( "attr1",
                            "value1" );

        attrs.setAttribute( "attr2",
                            "value2" );

        CoreChangeSet changeSet = new CoreChangeSet( null );

        executor.transferAttributes( changeSet,
                                     processCase,
                                     attrs );

        assertContains( processCase,
                        changeSet.getCoreModifiedCases() );

        assertEquals( "value1",
                      processCase.getAttribute( "attr1" ) );

        assertEquals( "value2",
                      processCase.getAttribute( "attr2" ) );
    }

    public void testExecute()
        throws Exception
    {
        ProcessPersistenceManager persistManager = new FleetingProcessPersistenceManager( "the.pkg",
                                                                                          "the.process" );

        CoreProcessCase processCase = new CoreProcessCase( null,
                                                           new MockChangeSetSource(),
                                                           null,
                                                           null,
                                                           new MockProcessInfo( "the.package",
                                                                                "the.process",
                                                                                "the docs" ),
                                                           persistManager.newCase( Attributes.EMPTY_ATTRIBUTES ) );

        processCase.addToken( "token.1" );
        processCase.addToken( "token.2" );

        DefaultNet net = new DefaultNet();
        DefaultTransition transition = net.addTransition( "the.transition" );

        DefaultTask task = new DefaultTask();

        task.setAction( new Action()
            {
                public void perform(ActionInvocation invocation)
                {
                    invocation.complete();
                }
            } );

        transition.setTask( task );

        CoreWorkItem workItem = new CoreWorkItem( processCase,
                                                  transition,
                                                  new String[]
            {
                "token.1",
                "token.2",
            }) ;

        CoreActivity activity = new CoreActivity( workItem,
                                                  null );

        Executor executor = new Executor();

        executor.execute( activity );
    }
}
