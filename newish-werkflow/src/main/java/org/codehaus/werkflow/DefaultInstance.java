package org.codehaus.werkflow;

import java.util.Map;
import java.util.HashMap;

public class DefaultInstance
    implements RobustInstance 
{
    private transient Engine engine;
    private transient Workflow workflow;

    private String id;
    private Map context;

    private Scope scope;

    private boolean complete;

    protected DefaultInstance(Engine engine,
                              Workflow workflow,
                              String id)
    {
        this.engine   = engine;
        this.workflow = workflow;
        this.id       = id;

        this.context  = new HashMap();
        this.scope    = new Scope( 0 );
    }

    public Engine getEngine()
    {
        return this.engine;
    }

    public Workflow getWorkflow()
    {
        return this.workflow;
    }

    public String getId()
    {
        return this.id;
    }

    public void put(String id,
                    Object value)
    {
        this.context.put( id,
                          value );
    }

    public Object get(String id)
    {
        return this.context.get( id );
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

    public synchronized void push(Path path)
    {
        //System.err.println( "push(" + path + ")" );
        Path parentPath = path.parentPath();

        Scope scope = null;

        if ( parentPath == null )
        {
            scope = this.scope;
        }
        else
        {
            scope = getScope( parentPath );
        }

        scope.newChild( path.tail() );
    }

    public synchronized void push(Path[] paths)
    {
        Path parentPath = paths[ 0 ].parentPath();

        Scope scope = null;

        if ( parentPath == null )
        {
            scope = this.scope;
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
        //System.err.println( "pop(" + path + ")" );

        Path parentPath = path.parentPath();

        Scope scope = null;

        if ( parentPath == null )
        {
            scope = this.scope;
        }
        else
        {
            scope = getScope( path.parentPath() );
        }

        Scope target = scope.getChild( path.tail() );

        //System.err.println( "root = " + this.scope );
        //System.err.println( "parent = " + scope );
        //System.err.println( "target = " + target );

        if ( target.getChildren().length != 0 )
        {
            throw new AssumptionViolationError( "can't pop if not stop of stack" );
        }

        scope.removeChild( path.tail() );
    }

    public synchronized void waitFor()
        throws InterruptedException
    {
        while ( ! this.complete )
        {
            wait();
        }
    }

    public synchronized void setComplete(boolean complete)
    {
        this.complete = complete;
        notifyAll();
    }

    public synchronized Scope getScope(Path path)
    {
        int[] segments = path.getSegments();

        Scope cur = scope;

        for ( int i = 0 ; i < segments.length ; ++i )
        {
            cur = cur.getChild( segments[ i ] );
        }

        return cur;
    }
}
