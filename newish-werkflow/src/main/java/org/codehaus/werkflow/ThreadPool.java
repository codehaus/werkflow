package org.codehaus.werkflow;

import java.util.LinkedList;
import java.util.Iterator;

class ThreadPool
{
    private Engine engine;
    private int numThreads;
    private Thread[] threads;
    private LinkedList queue;

    ThreadPool(Engine engine,
               int numThreads)
    {
        this.engine     = engine;
        this.numThreads = numThreads;
        this.queue      = new LinkedList();
    }

    Engine getEngine()
    {
        return this.engine;
    }

    synchronized void enqueue(InstanceTask task)
    {
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

        return (InstanceTask) this.queue.removeFirst();
    }

    synchronized boolean isActive(Instance instance)
    {
        for ( Iterator taskIter = this.queue.iterator();
              taskIter.hasNext(); )
        {
            InstanceTask task = (InstanceTask) taskIter.next();

            if ( task.getInstance().getId().equals( instance.getId() ) )
            {
                return true;
            }
        }

        return false;
    }

    void start()
    {
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
                                getEngine().run( task.getInstance(),
                                                 task.getPath() );
                            }
                            catch (InterruptedException e)
                            {
                                break LOOP;
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                };

            this.threads[ i ].setDaemon( false );
            this.threads[ i ].start();
        }
    }

    void stop()
    {
        for ( int i = 0 ; i < this.threads.length ; ++i )
        {
            this.threads[ i ].interrupt();
        }
    }
}
