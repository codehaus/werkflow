package com.werken.werkflow.core;

/*
 $Id$

 Copyright 2003 (C) The Codehaus. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "werkflow" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Codehaus.  For written permission,
    please contact info@codehaus.org.

 4. Products derived from this Software may not be called "werkflow"
    nor may "werkflow" appear in their names without prior written
    permission of The Codehaus. "werkflow" is a registered
    trademark of The Codehaus.

 5. Due credit should be given to The Codehaus -
    http://werkflow.codehaus.org/

 THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

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
    }
}
