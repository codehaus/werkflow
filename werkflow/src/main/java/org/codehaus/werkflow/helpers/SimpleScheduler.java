/*
 * $Id$
 */

package org.codehaus.werkflow.helpers;

import java.util.LinkedList;

import org.codehaus.werkflow.Engine;
import org.codehaus.werkflow.InstanceTask;
import org.codehaus.werkflow.NoSuchInstanceException;
import org.codehaus.werkflow.spi.Scheduler;

/**
 * SimpleScheduler
 *
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class SimpleScheduler
    implements Scheduler
{
    private final Engine engine;
    private LinkedList queue;
    
    public SimpleScheduler(Engine engine)
    {
        this.engine = engine;
        this.queue  = new LinkedList();
    }

    public void start(Engine engine)
    {
        if ( this.engine != engine )
        {
            throw new IllegalArgumentException( "unknown engine" );
        }
    }

    public void stop(Engine engine)
    {
        if ( this.engine != engine )
        {
            throw new IllegalArgumentException( "unknown engine" );
        }
    }

    public synchronized void enqueue(InstanceTask task)
    {
        // hmm; ThreadPool drops tasks that for instances that are already
        // on the queue. don't see why just yet.

        queue.addLast( task );
    }

    private synchronized InstanceTask dequeue()
    {
        if ( queue.size() == 0 )
        {
            return null;
        }

        return (InstanceTask) queue.removeFirst();
    }

    public boolean runTask()
        throws InterruptedException
    {
        InstanceTask task = dequeue();

        if ( task == null )
        {
            return false;
        }

        try
        {
            this.engine.run( task );
        }
        catch (NoSuchInstanceException e)
        {
            System.err.println( "ERROR: " + e.getMessage() );

            return false;
        }

        return true;
    }
}
