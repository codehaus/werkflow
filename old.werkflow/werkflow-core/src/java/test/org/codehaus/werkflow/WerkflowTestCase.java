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

import org.codehaus.werkflow.admin.WfmsAdmin;
import org.codehaus.werkflow.definition.ProcessDefinition;
import org.codehaus.werkflow.engine.WorkflowEngine;
import org.codehaus.werkflow.service.SimpleWfmsServices;
import org.codehaus.werkflow.service.messaging.simple.SimpleMessagingManager;
import org.codehaus.werkflow.service.persistence.PersistenceManager;
import org.codehaus.werkflow.service.persistence.fleeting.FleetingPersistenceManager;
import org.codehaus.werkflow.syntax.fundamental.FundamentalDefinitionLoader;

import junit.framework.TestCase;

import java.net.URL;
import java.util.Arrays;

/**
 * Common base class for werkflow unit tests. Provides basic services that
 * are common to test case start-up and execution (initializing the werkflow
 * engine, loading processes and such like).
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *  @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 *
 *  @version $Id$
 */
public class WerkflowTestCase
    extends TestCase
{
    /** Workflow services. */
    protected SimpleWfmsServices services;

    /** Workflow persistence manager. */
    protected PersistenceManager persistenceManager;

    /** Workflow messaging manager. */
    protected SimpleMessagingManager messagingManager;

    /** Workflow engine. */
    protected WorkflowEngine engine;

    protected void setUp()
        throws Exception
    {
        services = new SimpleWfmsServices();

        messagingManager = new SimpleMessagingManager();

        persistenceManager = new FleetingPersistenceManager();

        services.setMessagingManager( messagingManager );

        services.setPersistenceManager( persistenceManager );

        engine = new WorkflowEngine( services );
    }

    protected void deployFundamentalProcess( String file,
                                             int expectedProcesses)
        throws Exception
    {
        URL url = getClass().getResource( file );
    
        FundamentalDefinitionLoader loader = new FundamentalDefinitionLoader();
    
        ProcessDefinition[] processDefs = loader.load( url );
    
        WfmsAdmin admin = engine.getAdmin();
    
        assertLength( expectedProcesses,
                      processDefs );
    
        for ( int i = 0 ; i < processDefs.length ; ++i )
        {
            admin.deployProcess( processDefs[i] );
        }
    }

    public static class Relay
    {
        private Object value;

        public Relay()
        {

        }

        public Relay(Object value)
        {
            this.value = value;
        }

        public Object getValue()
        {
            return value;
        }

        public void setValue(Object value)
        {
            this.value = value;
        }
    }

    protected void assertContains(Object obj,
                                  Object[] array)
    {
        for ( int i = 0 ; i < array.length ; ++i )
        {
            if ( array[i].equals( obj ) )
            {
                return;
            }
        }

        fail( obj + " not in " + Arrays.asList( array ) );
    }

    protected void assertContains(Object[] expected,
                                  Object[] actual)
    {
        for ( int i = 0 ; i < expected.length ; ++i )
        {
            assertContains( expected[i],
                            actual );
        }
    }

    protected void assertEmpty(Object[] array)
    {
        assertEquals( 0,
                      array.length );
    }

    protected void assertLength(int len,
                                Object[] array)
    {
        assertEquals( len,
                      array.length );
    }

    protected void dumpArray(String message,
                             Object[] array)
    {
        System.err.println( message + ": " + Arrays.asList( array ) );
    }

}
