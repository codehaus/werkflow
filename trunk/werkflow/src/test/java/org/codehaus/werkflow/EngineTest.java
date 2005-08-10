/*
 * $Id$
 */

package org.codehaus.werkflow;

import org.codehaus.werkflow.helpers.SimpleInstanceManager;
import org.codehaus.werkflow.helpers.SimplePersistenceManager;
import org.codehaus.werkflow.helpers.SimpleSatisfactionManager;
import org.codehaus.werkflow.helpers.SimpleWorkflowManager;
import org.codehaus.werkflow.helpers.ThreadPoolScheduler;
import org.codehaus.werkflow.spi.InstanceManager;
import org.codehaus.werkflow.spi.PersistenceManager;
import org.codehaus.werkflow.spi.SatisfactionManager;
import org.codehaus.werkflow.spi.Scheduler;
import org.codehaus.werkflow.spi.WorkflowManager;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class EngineTest
    extends WerkflowTestBase
{
    public void testDefaultEngineStart()
    {
        Engine engine = new Engine();

        engine.start();

        assertNotNull( engine.getPersistenceManager() );
        assertNotNull( engine.getWorkflowManager() );
        assertNotNull( engine.getSatisfactionManager() );
        assertNotNull( engine.getInstanceManager() );
        assertNotNull( engine.getScheduler() );
        assertTrue( engine.isStarted() );

    }

    public void testConfiguredEngineStart()
    {
        Engine engine = new Engine();

        PersistenceManager pm = new SimplePersistenceManager();
        WorkflowManager wm = new SimpleWorkflowManager();
        SatisfactionManager sm = new SimpleSatisfactionManager();
        InstanceManager im = new SimpleInstanceManager();
        Scheduler scheduler = new ThreadPoolScheduler( 5 );

        engine.setPersistenceManager( pm );
        engine.setWorkflowManager( wm );
        engine.setSatisfactionManager( sm );
        engine.setInstanceManager( im );
        engine.setScheduler( scheduler );

        engine.start();

        assertEquals( pm,
                      engine.getPersistenceManager() );
        assertEquals( wm,
                      engine.getWorkflowManager() );
        assertEquals( sm,
                      engine.getSatisfactionManager() );
        assertEquals( im,
                      engine.getInstanceManager() );
        assertEquals( scheduler,
                      engine.getScheduler() );
        assertTrue( engine.isStarted() );
    }

    public void testPostStartConfiguration()
    {
        Engine engine = new Engine();

        engine.start();

        try
        {
            engine.setPersistenceManager( new SimplePersistenceManager() );

            fail();
        }
        catch ( IllegalStateException e )
        {
            // expected
        }

        try
        {
            engine.setWorkflowManager( new SimpleWorkflowManager() );

            fail();
        }
        catch ( IllegalStateException e )
        {
            // expected
        }

        try
        {
            engine.setSatisfactionManager( new SimpleSatisfactionManager() );

            fail();
        }
        catch ( IllegalStateException e )
        {
            // expected
        }

        try
        {
            engine.setInstanceManager( new SimpleInstanceManager() );

            fail();
        }
        catch ( IllegalStateException e )
        {
            // expected
        }

        try
        {
            engine.setScheduler( new ThreadPoolScheduler( 5 ) );

            fail();
        }
        catch ( IllegalStateException e )
        {
            // expected
        }
    }
}
