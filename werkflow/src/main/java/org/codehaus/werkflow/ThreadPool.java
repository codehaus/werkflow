package org.codehaus.werkflow;

import org.codehaus.werkflow.spi.RobustInstance;
import org.codehaus.werkflow.spi.RobustTransaction;

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
                            //getEngine().getPersistenceManager().beginTransaction();

                            try
                            {
                                InstanceTask task = dequeue();

                                getEngine().initializeThreadPoolThread();

                                RobustTransaction transaction = getEngine().beginThreadPoolTransaction( task.getInstanceId() );

                                try
                                {
                                    transaction.run( task.getPath() );
                                    
                                    getEngine().getPersistenceManager().commitTransaction();
                                    //getEngine().getPersistenceManager().beginTransaction();

                                    transaction.commit();

                                    //getEngine().getPersistenceManager().commitTransaction();
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                finally
                                {
                                    if ( getEngine().getPersistenceManager().isTransactionActive() )
                                    {
                                        getEngine().getPersistenceManager().rollbackTransaction();
                                    }

                                    if ( transaction.isOpen() )
                                    {
                                        transaction.rollback();
                                    }
                                }
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
    
    void stop()
    {
        for ( int i = 0 ; i < this.threads.length ; ++i )
        {
            this.threads[ i ].interrupt();
        }
    }
}
