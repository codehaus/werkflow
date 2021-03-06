package org.codehaus.werkflow;

import org.codehaus.werkflow.spi.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class ManualEngine
    extends Engine
{
    private static final Path[] EMPTY_PATH_ARRAY = new Path[ 0 ];

    private Map queues;
    private Set polls;

    public ManualEngine(InstanceManager instanceManager)
    {
        this.queues = new HashMap();
        this.polls  = new HashSet();

        setInstanceManager( instanceManager );
    }

    public ManualEngine(InstanceManager instanceManager,
                        SatisfactionManager satisfactionManager)
    {
        this( instanceManager );
        setSatisfactionManager( satisfactionManager );
    }

    protected void enqueue(final RobustInstance instance,
                           final Path path)
    {
        instance.enqueue( path );
        LinkedList list = (LinkedList) this.queues.get( instance.getId() );

        if ( list == null )
        {
            list = new LinkedList();
            this.queues.put( instance.getId() ,
                             list );
        }

        list.addLast( path );
    }

    protected boolean isActive(RobustInstance instance)
    {
        return ( getEnqueued( instance ).length > 0 );
    }

    Path[] getEnqueued(Instance instance)
    {
        LinkedList list = (LinkedList) this.queues.get( instance.getId() );

        if ( list == null )
        {
            return EMPTY_PATH_ARRAY;
        }

        return (Path[]) list.toArray( EMPTY_PATH_ARRAY );
    }

    public boolean step(Instance instance)
        throws Exception
    {
        LinkedList list = (LinkedList) this.queues.get( instance.getId() );

        if ( list == null
             ||
             list.isEmpty() )
        {
            return false;
        }

        Path path = (Path) list.removeFirst();

        run( (RobustInstance) instance,
             path );

        return true;
    }

    public void poll()
    {
        for ( Iterator pollIter = this.polls.iterator();
              pollIter.hasNext(); )
        {
            Runnable poll = (Runnable) pollIter.next();
            poll.run();
        }
    }

    protected void setupSatisfactionPoll(final PolledSatisfaction satisfaction,
                                         final String instanceId)
    {
        this.polls.add( new Runnable()
            {
                public void run()
                {
                    try
                    {
                        if ( getSatisfactionManager().isSatisfied( satisfaction.getId(),
                                                                   getInstance( instanceId ) ) )
                        {
                            cancelSatisfactionPoll( this );
                            
                            satisfy( satisfaction.getId(),
                                     instanceId );
                        }
                    }
                    catch (InterruptedException e)
                    {
                        cancelSatisfactionPoll( this );
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        cancelSatisfactionPoll( this );
                    }
                }
            } );
    }

    protected void cancelSatisfactionPoll(Runnable poll)
    {
        this.polls.remove( poll );
    }
}
