package org.codehaus.werkflow.initiator.message;

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

import org.codehaus.werkflow.WerkflowTestCase;
import org.codehaus.werkflow.ProcessCase;
import org.codehaus.werkflow.admin.WfmsAdmin;
import org.codehaus.werkflow.definition.ProcessDefinition;
import org.codehaus.werkflow.engine.WorkflowEngine;
import org.codehaus.werkflow.personality.Personality;
import org.codehaus.werkflow.personality.basic.BasicPersonality;
import org.codehaus.werkflow.service.SimpleWfmsServices;
import org.codehaus.werkflow.service.messaging.simple.SimpleMessagingManager;
import org.codehaus.werkflow.service.persistence.PersistenceManager;
import org.codehaus.werkflow.service.persistence.fleeting.FleetingPersistenceManager;

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

        beans.put( "roleSelector", new RoleSelector() );

        Personality bp = BasicPersonality.getInstance();

        ProcessDefinition[] processDefs = bp.load( url, beans );

        WfmsAdmin admin = engine.getAdmin();

        for ( int i = 0 ; i < processDefs.length ; ++i )
        {
            admin.deployProcess( processDefs[i] );

            System.out.println( "processDefs = " + processDefs[i] );
        }

        Entity entity = new Entity();

        messagingManager.acceptMessage( entity );

        // We need to sleep to let the message processing occur.
        Thread.sleep( 5000 );

        assertTrue( "The entity has not been touched!", entity.hasBeenTouched() );

        assertTrue( "action-2 has not been executed.", entity.hasActionExecuted( "action-2" ) );
    }
}
