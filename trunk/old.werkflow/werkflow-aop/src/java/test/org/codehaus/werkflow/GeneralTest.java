package com.werken.werkflow;

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

import com.werken.werkflow.WfmsRuntime;
import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.SimpleAttributes;
import com.werken.werkflow.admin.WfmsAdmin;
import com.werken.werkflow.engine.WorkflowEngine;
import com.werken.werkflow.service.WfmsServices;
import com.werken.werkflow.service.SimpleWfmsServices;
import com.werken.werkflow.service.messaging.simple.SimpleMessagingManager;
import com.werken.werkflow.service.persistence.PersistenceManager;
import com.werken.werkflow.service.persistence.fleeting.FleetingPersistenceManager;
import com.werken.werkflow.syntax.fundamental.FundamentalDefinitionLoader;
import com.werken.werkflow.definition.ProcessDefinition;

import java.net.URL;

public class GeneralTest
    extends WerkflowTestCase
{
    /*
    public void testGeneral2()
        throws Exception
    {
        SimpleWfmsServices services = new SimpleWfmsServices();

        SimpleMessagingManager messagingManager = new SimpleMessagingManager();
        PersistenceManager persistenceManager = new FleetingPersistenceManager();

        services.setMessagingManager( messagingManager );
        services.setPersistenceManager( persistenceManager );

        WorkflowEngine engine = new WorkflowEngine( services );

        URL url = getClass().getResource( "general2.xml" );

        FundamentalDefinitionLoader loader = new FundamentalDefinitionLoader();

        ProcessDefinition[] processDefs = loader.load( url );

        WfmsAdmin admin = engine.getAdmin();

        assertLength( 1,
                      processDefs );

        for ( int i = 0 ; i < processDefs.length ; ++i )
        {
            // System.err.println( processDefs[i] );
            admin.deployProcess( processDefs[i] );
        }

        WfmsRuntime runtime = engine.getRuntime();

        SimpleAttributes attrs = new SimpleAttributes();

        ProcessCase processCase = runtime.callProcess( "",
                                                       "general",
                                                       attrs );

        assertNotNull( processCase );
    }
    */

    public void testGeneral3()
        throws Exception
    {
        SimpleWfmsServices services = new SimpleWfmsServices();

        SimpleMessagingManager messagingManager = new SimpleMessagingManager();
        PersistenceManager persistenceManager = new FleetingPersistenceManager();

        services.setMessagingManager( messagingManager );
        services.setPersistenceManager( persistenceManager );

        WorkflowEngine engine = new WorkflowEngine( services );

        URL url = getClass().getResource( "general3.xml" );

        FundamentalDefinitionLoader loader = new FundamentalDefinitionLoader();

        ProcessDefinition[] processDefs = loader.load( url );

        WfmsAdmin admin = engine.getAdmin();

        assertLength( 1,
                      processDefs );

        for ( int i = 0 ; i < processDefs.length ; ++i )
        {
            // System.err.println( processDefs[i] );
            admin.deployProcess( processDefs[i] );
        }

        WfmsRuntime runtime = engine.getRuntime();

        SimpleAttributes attrs = new SimpleAttributes();

        ProcessCase processCase = runtime.callProcess( "",
                                                       "general",
                                                       attrs );



        assertNotNull( processCase );

        messagingManager.acceptMessage( new SimpleMessage( "normal",
                                                           "mr mcwhirter" ) );

        Thread.sleep( 1000 );
    }
}
