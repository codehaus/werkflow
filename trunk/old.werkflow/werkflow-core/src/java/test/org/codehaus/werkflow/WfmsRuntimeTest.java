package org.codehaus.werkflow;

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

import org.codehaus.werkflow.WfmsRuntime;
import org.codehaus.werkflow.ProcessCase;
import org.codehaus.werkflow.SimpleAttributes;
import org.codehaus.werkflow.admin.WfmsAdmin;
import org.codehaus.werkflow.engine.WorkflowEngine;
import org.codehaus.werkflow.service.SimpleWfmsServices;
import org.codehaus.werkflow.service.messaging.simple.SimpleMessagingManager;
import org.codehaus.werkflow.service.persistence.PersistenceManager;
import org.codehaus.werkflow.service.persistence.fleeting.FleetingPersistenceManager;
import org.codehaus.werkflow.definition.ProcessDefinition;
import org.codehaus.werkflow.syntax.fundamental.FundamentalDefinitionLoader;

import java.net.URL;

public class WfmsRuntimeTest
    extends WerkflowTestCase
{        
    
    public void testSelectCasesNotNull()
        throws Exception
    {
        SimpleWfmsServices services = new SimpleWfmsServices();

        SimpleMessagingManager messagingManager = new SimpleMessagingManager();
        PersistenceManager persistenceManager = new FleetingPersistenceManager();

        services.setMessagingManager( messagingManager );
        services.setPersistenceManager( persistenceManager );

        WorkflowEngine engine = new WorkflowEngine( services );

        URL url = getClass().getResource( "runtime.xml" );

        FundamentalDefinitionLoader loader = new FundamentalDefinitionLoader();

        ProcessDefinition[] processDefs = loader.load( url );
                
        WfmsAdmin admin = engine.getAdmin();

        admin.deployProcess( processDefs[0] );

        WfmsRuntime runtime = engine.getRuntime();

        String anObjectId = "2213";
        
        // create a new case
        
        SimpleAttributes attrs = new SimpleAttributes();
        attrs.setAttribute( "anObjectId", anObjectId );

        ProcessCase processCase = runtime.callProcess( processDefs[0].getPackageId(), 
                                                       processDefs[0].getId(),
                                                       attrs );
        
        assertNotNull( processCase );
        
        Thread.sleep( 2000 );
        
        // now try to find the case
        
        SimpleAttributes attrs2 = new SimpleAttributes();
        attrs2.setAttribute( "anObjectId", anObjectId );
        
        ProcessCase[] pc = runtime.selectCases( processDefs[0].getPackageId(), 
                                                processDefs[0].getId(),
                                                attrs2 );        
        
        assertNotNull( "There should be at least one ProcessCase", pc );
                
    }

}
