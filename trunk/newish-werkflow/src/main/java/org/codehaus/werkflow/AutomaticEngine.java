package org.codehaus.werkflow;

//import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
//import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import java.util.Timer;
import java.util.TimerTask;

public class AutomaticEngine
    extends Engine
{
    private Timer timer;
    //private PooledExecutor pool;
    private ThreadPool pool;

    public AutomaticEngine()
    {
        this.timer = new Timer();
        //this.pool  = new PooledExecutor( new LinkedQueue() );
        //this.pool.setKeepAliveTime(-1); 
        //this.pool.createThreads(1);
        this.pool = new ThreadPool( this,
                                    1 );

        this.pool.start();
    }

    public AutomaticEngine(InstanceManager instanceManager)
    {
        this();
        setInstanceManager( instanceManager );
    }

    public AutomaticEngine(InstanceManager instanceManager,
                           SatisfactionManager satisfactionManager)
    {
        this();
        setInstanceManager( instanceManager );
        setSatisfactionManager( satisfactionManager );
    }

    protected boolean isActive(RobustInstance instance)
    {
        return this.pool.isActive( instance );
    }

    protected void enqueue(RobustInstance instance,
                           Path path)
        throws InterruptedException
    {
        instance.enqueue( path );
        InstanceTask task = new InstanceTask( instance,
                                              path );
        this.pool.enqueue( task );
    }

    protected void setupSatisfactionPoll(final PolledSatisfaction satisfaction,
                                         final String instanceId)
    {
        long interval = satisfaction.getInterval();

        TimerTask poll = new TimerTask()
            {
                public void run()
                {
                    try
                    {
                        if ( getSatisfactionManager().isSatisfied( satisfaction.getId(),
                                                                  (RobustInstance) getInstance( instanceId ) ) )
                        {
                            cancel();
                            
                            satisfy( satisfaction.getId(),
                                     instanceId );
                        }
                    }
                    catch (InterruptedException e)
                    {
                        cancel();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        cancel();
                    }
                }
            };

        this.timer.schedule( poll,
                             0L,
                             satisfaction.getInterval() );
    }
}
