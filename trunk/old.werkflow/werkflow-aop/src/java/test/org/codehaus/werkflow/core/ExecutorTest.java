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

        CoreActivity[] activities = new CoreActivity[] {
            new CoreActivity( workItem,
                              null )
        };

        Executor executor = new Executor();

        executor.enqueueActivities( activities );
    }
}
