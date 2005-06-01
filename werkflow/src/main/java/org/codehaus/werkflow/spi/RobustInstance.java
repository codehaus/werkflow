package org.codehaus.werkflow.spi;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.codehaus.werkflow.AssumptionViolationError;
import org.codehaus.werkflow.InitialContext;
import org.codehaus.werkflow.ReadOnlyInstance;
import org.codehaus.werkflow.Workflow;

public class RobustInstance
    implements ReadOnlyInstance, Instance, Serializable
{
    private Throwable error;

    private RobustInstanceState state;

    public RobustInstance(RobustInstanceState state)
    {
        this.state = state;
    }

    public RobustInstance(Workflow workflow,
                          String id,
                          InitialContext context)
    {
        this.state = new RobustInstanceState();

        this.state.setWorkflow( workflow );
        this.state.setWorkflowId( workflow.getId() );
        this.state.setId( id );
        this.state.setContext( new HashMap( context.getContextMap()  ) );
    }

    public boolean hasError()
    {
        return ( error != null );
    }

    public void setError( Throwable error )
    {
        this.error = error;
    }

    public Throwable getError()
    {
        return error;
    }

    public RobustInstanceState getState()
    {
        return this.state;
    }

    public void setState(RobustInstanceState state)
    {
        this.state = state;
    }

    public Workflow getWorkflow()
    {
        return this.state.getWorkflow();
    }

    public String getWorkflowId()
    {
        return this.state.getWorkflowId();
    }

    public String getId()
    {
        return this.state.getId();
    }

    public void put(String id,
                    Object value)
    {
        this.state.getContext().put( id,
                                     value );
    }

    public Object get(String id)
    {
        return this.state.getContext().get( id );
    }

    public Map getContextMap()
    {
        return this.state.getContext();
    }

    public synchronized Path[] getActiveChildren(Path path)
    {
        Scope scope = getScope( path );

        Scope[] childScopes = scope.getChildren();

        Path[] childPaths = new Path[ childScopes.length ];

        for ( int i = 0 ; i < childPaths.length ; ++i )
        {
            childPaths[ i ] = path.childPath( childScopes[ i ].getSegment() );
        }

        return childPaths;
    }

    public synchronized Path[] getLeaves()
    {
        return this.state.getScope().getLeaves();
    }

    public synchronized SatisfactionSpec[] getBlockedSatisfactions()
    {
        Workflow workflow = getWorkflow();

        Set blocked = new HashSet();

        Path[] leaves = getLeaves();

        for ( int i = 0 ; i < leaves.length ; ++i )
        {
            Component leaf = workflow.getComponent( leaves[ i ] );

            if ( leaf instanceof Satisfaction )
            {
                SatisfactionSpec spec = ((Satisfaction)leaf).getSatisfactionSpec();

                blocked.add( spec );
            }
        }

        return (SatisfactionSpec[]) blocked.toArray( new SatisfactionSpec[ blocked.size() ] );
    }

    public void addPendingSatisfactionId(String id)
    {
        getState().getPendingSatisfactionIds().add( id );
    }

    public void removePendingSatisfactionId(String id)
    {
        getState().getPendingSatisfactionIds().remove( id );
    }

    public synchronized String[] getPendingSatisfactionIds()
    {
        Set pending = getState().getPendingSatisfactionIds();

        return (String[]) pending.toArray( new String[ pending.size() ] );
    }

    public synchronized SatisfactionSpec[] getEligibleSatisfactions()
    {
        Set pending = getState().getPendingSatisfactionIds();
        Set eligible = new HashSet();

        SatisfactionSpec[] blocked = getBlockedSatisfactions();

       // System.err.println( "blocked: " + Arrays.asList( blocked ) );
       // System.err.println( "pending: " + pending );

        for ( int i = 0 ; i < blocked.length ; ++i )
        {
            if ( ! pending.contains( blocked[i].getId() ) )
            {
                eligible.add( blocked[ i ] );
            }
        }

        //System.err.println( "eligible: " + eligible );

        return (SatisfactionSpec[]) eligible.toArray( new SatisfactionSpec[ eligible.size() ] );
    }

    public SatisfactionSpec getBlockedSatisfaction(String id)
    {
        SatisfactionSpec[] satisfactions = getBlockedSatisfactions();

        for ( int i = 0 ; i < satisfactions.length ; ++i )
        {
            if ( satisfactions[ i ].getId().equals( id ) )
            {
                return satisfactions[ i ];
            }
        }

        return null;
    }

    public synchronized void push(Path path)
    {
        if ( path.getSegments().length == 1
             &&
             path.getSegments()[0] == 0 )
        {
            return;
        }

        Path parentPath = path.parentPath();

        Scope scope = null;

        if ( parentPath == null )
        {
            scope = this.state.getScope();
        }
        else
        {
            scope = getScope( parentPath );
        }

        scope.newChild( path.tail() );
    }

    public synchronized void push(Path[] paths)
    {
        if ( paths.length == 1
             &&
             paths[0].getSegments().length == 1
             &&
             paths[0].getSegments()[0] == 0 )
        {
            return;
        }

        Path parentPath = paths[ 0 ].parentPath();

        Scope scope = null;

        if ( parentPath == null )
        {
            scope = this.state.getScope();
        }
        else
        {
            scope = getScope( parentPath );
        }

        int[] tails = new int[ paths.length ];

        for ( int i = 0 ; i < tails.length ; ++i )
        {
            tails[ i ] = paths[ i ].tail();
        }

        scope.newChildren( tails );
    }

    public synchronized void pop(Path path)
    {
        Path parentPath = path.parentPath();

        Scope scope = null;

        if ( parentPath == null )
        {
            scope = this.state.getScope();
        }
        else
        {
            scope = getScope( path.parentPath() );
        }

        Scope target = scope.getChild( path.tail() );

        if ( target.getChildren().length != 0 )
        {
            throw new AssumptionViolationError( "can't pop if not stop of stack" );
        }

        scope.removeChild( path.tail() );
    }

    public synchronized void waitFor()
        throws InterruptedException
    {
        while ( ! this.state.getComplete() )
        {
            wait();
        }
    }

    public synchronized void setComplete(boolean complete)
    {
        this.state.setComplete( complete );
        notifyAll();
    }

    public boolean isComplete()
    {
        return this.state.getComplete();
    }

    public synchronized Scope getScope(Path path)
    {
        int[] segments = path.getSegments();

        Scope cur = this.state.getScope();

        for ( int i = 1 ; i < segments.length ; ++i )
        {
            cur = cur.getChild( segments[ i ] );
        }

        return cur;
    }

    public void enqueue(Path path)
    {
        this.state.getQueue().add( path );
    }

    public void dequeue(Path path)
    {
        this.state.getQueue().remove( path );
    }

    public Path[] getQueue()
    {
        return (Path[]) this.state.getQueue().toArray( new Path[ this.state.getQueue().size() ] );
    }

    public Path getNextPath()
    {
        if ( this.state.getQueue().isEmpty() )
        {
            return null;
        }

        return (Path) this.state.getQueue().iterator().next();
    }

    public String toString()
    {
        return "[" + getId() + "| " + this.state.getQueue() + "]";
    }
}
