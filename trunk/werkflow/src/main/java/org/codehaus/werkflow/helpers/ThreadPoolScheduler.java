package org.codehaus.werkflow.helpers;

import org.codehaus.werkflow.Engine;
import org.codehaus.werkflow.InstanceTask;
import org.codehaus.werkflow.NoSuchInstanceException;
import org.codehaus.werkflow.spi.Scheduler;

import java.util.LinkedList;
import java.util.Iterator;

public class ThreadPoolScheduler
    implements Scheduler
{
    private Engine engine;
    private int numThreads;
    private Thread[] threads;
    private LinkedList queue;

    public ThreadPoolScheduler(int numThreads)
    {
        this.numThreads = numThreads;
        this.queue      = new LinkedList();
    }

    public void start(Engine engine)
    {
        if ( ThreadPoolScheduler.this.engine != null )
        {
            throw new IllegalStateException( "scheduler already started" );
        }

        ThreadPoolScheduler.this.engine = engine;

        this.threads = new Thread[ numThreads ];

        for ( int i = 0 ; i < this.threads.length ; ++i )
        {
            this.threads[ i ] = new Thread()
                {
                    public void run()
                    {

                      LOOP:
                        while ( true )
                        {
                            try
                            {
                                InstanceTask task = dequeue();

                                ThreadPoolScheduler.this.engine.run( task );
                            }
                            catch (InterruptedException e)
                            {
                                break LOOP;
                            }
                            catch (NoSuchInstanceException e)
                            {
                                System.err.println( "ERROR: " + e.getMessage() );
                                // Not fatal, loop again
                            }
                            catch (Exception e)
                            {
                                System.err.println( "ERROR: " + e.getMessage() );
                                //e.printStackTrace();
                                // Not fatal, loop again
                            }
                        }
                    }
                };
            this.threads[ i ].setDaemon( false );
            this.threads[ i ].start();
        }
    }
    
    public void stop(Engine engine)
    {
        if ( ThreadPoolScheduler.this.engine == null )
        {
            throw new IllegalStateException( "scheduler already stopped" );
        }
        if ( ThreadPoolScheduler.this.engine != engine )
        {
            throw new IllegalArgumentException( "unknown engine " + engine + " - attached to " + ThreadPoolScheduler.this.engine );
        }

        ThreadPoolScheduler.this.engine = null;

        for ( int i = 0 ; i < this.threads.length ; ++i )
        {
            this.threads[ i ].interrupt();
        }
    }

    public synchronized void enqueue(InstanceTask task)
    {
        for ( Iterator taskIter = this.queue.iterator();
              taskIter.hasNext(); )
        {
            InstanceTask inList = (InstanceTask) taskIter.next();

            if ( inList.getInstanceId().equals( task.getInstanceId() ) )
            {
                return;
            }
        }

        this.queue.addLast( task );
        notifyAll();
    }

    synchronized InstanceTask dequeue()
        throws InterruptedException
    {
        while ( this.queue.isEmpty() )
        {
            wait();
        }

        InstanceTask task = (InstanceTask) this.queue.removeFirst();

        return task;
    }
}
