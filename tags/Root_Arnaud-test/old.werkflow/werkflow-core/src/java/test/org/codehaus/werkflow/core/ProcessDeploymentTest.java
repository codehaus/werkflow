package org.codehaus.werkflow.core;

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

import org.codehaus.werkflow.definition.ProcessDefinition;
import org.codehaus.werkflow.service.messaging.MessagingManager;
import org.codehaus.werkflow.service.messaging.simple.SimpleMessagingManager;
import org.codehaus.werkflow.service.persistence.PersistenceManager;
import org.codehaus.werkflow.service.persistence.PersistenceException;
import org.codehaus.werkflow.service.persistence.fleeting.FleetingPersistenceManager;

import org.codehaus.werkflow.WerkflowTestCase;

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
                                                              persistManager.activate( processDef ),
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
                                                              persistManager.activate( processDef ),
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
