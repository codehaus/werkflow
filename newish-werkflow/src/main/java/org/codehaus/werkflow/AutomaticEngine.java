package org.codehaus.werkflow;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import java.util.Timer;
import java.util.TimerTask;

public class AutomaticEngine
    extends Engine
{
    private Timer timer;
    private PooledExecutor pool;

    public AutomaticEngine()
    {
        this.timer = new Timer();
        this.pool  = new PooledExecutor( new LinkedQueue() );
        this.pool.setKeepAliveTime(-1); 
        this.pool.createThreads(5);
    }

    public AutomaticEngine(SatisfactionManager satisfactionManager)
    {
        this();
        setSatisfactionManager( satisfactionManager );
    }

    protected void enqueue(final Instance instance,
                           final Path path)
        throws InterruptedException
    {
        Runnable task = new Runnable()
            {
                public void run()
                {
                    try
                    {
                        AutomaticEngine.this.run( instance,
                                                  path );
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            };

        this.pool.execute( task );
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
                                                                  getInstance( instanceId ) ) )
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
                    catch (NoSuchInstanceException e)
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
