package com.werken.werkflow.personality.basic;

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

import com.werken.werkflow.Wfms;
import com.werken.werkflow.SimpleAttributes;
import com.werken.werkflow.WfmsRuntime;
import com.werken.werkflow.admin.WfmsAdmin;
import com.werken.werkflow.engine.WorkflowEngine;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.personality.Personality;
import com.werken.werkflow.service.WfmsServices;
import com.werken.werkflow.service.SimpleWfmsServices;
import com.werken.werkflow.service.persistence.PersistenceManager;
import com.werken.werkflow.service.persistence.fleeting.FleetingPersistenceManager;
import com.werken.werkflow.service.messaging.simple.SimpleMessagingManager;

import junit.framework.TestCase;

import java.net.URL;

public class BasicPersonalityTest
    extends TestCase
{
    public void testLoad()
        throws Exception
    {
        Personality personality = BasicPersonality.getInstance();

        assertNotNull( personality );

        URL procUrl = getClass().getResource( "process1.xml" );

        ProcessDefinition[] procDefs = personality.load( procUrl );

        assertEquals( 1,
                      procDefs.length );

        assertEquals( "my.pkg",
                      procDefs[0].getPackageId() );

        assertEquals( "my.proc1",
                      procDefs[0].getId() );

        SimpleWfmsServices services = new SimpleWfmsServices();

        services.setPersistenceManager( new FleetingPersistenceManager() );

        SimpleMessagingManager msgManager = new SimpleMessagingManager();

        services.setMessagingManager( msgManager );

        Wfms wfms = new WorkflowEngine( services );

        WfmsAdmin admin = wfms.getAdmin();

        admin.deployProcess( procDefs[0] );

        WfmsRuntime runtime = wfms.getRuntime();

        SimpleAttributes attrs = new SimpleAttributes();


        /*
        runtime.newProcessCase( "my.proc1",
                                attrs );
        */

        // Thread.sleep( 2000 );

        msgManager.acceptMessage( "strachan" );
        msgManager.acceptMessage( "mcwhirter" );

        msgManager.acceptMessage( new Integer( 10 ) );
        msgManager.acceptMessage( new Integer( 9 ) );
        msgManager.acceptMessage( new Integer( 8 ) );

        Thread.sleep( 2000 );
    }
}
