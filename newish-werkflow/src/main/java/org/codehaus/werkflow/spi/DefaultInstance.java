package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.Engine;
import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.InitialContext;
import org.codehaus.werkflow.NoSuchWorkflowException;
import org.codehaus.werkflow.AssumptionViolationError;

import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class DefaultInstance
    implements RobustInstance, Serializable
{
    private String workflowId;
    private transient Workflow workflow;

    private String id;
    private Map context;

    private Scope scope;

    private Set queue;

    private boolean complete;

    public DefaultInstance(Workflow workflow,
                           String id,
                           InitialContext context)
    {
        this.workflowId = workflow.getId();
        this.workflow = workflow;
        this.id       = id;
        this.context  = new HashMap();
        this.context.putAll( context.getContextMap() );
        this.queue    = new HashSet();
        this.scope    = new Scope( 0 );
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

    public boolean isComplete()
    {
        return this.complete;
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

    public DefaultInstance duplicate()
        throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream( byteOut );
        
        objectOut.writeObject( this );
        objectOut.close();
        
        byte[] bytes = byteOut.toByteArray();
        
        ByteArrayInputStream byteIn = new ByteArrayInputStream( bytes );
        ObjectInputStream objectIn = new ObjectInputStream( byteIn );
        
        DefaultInstance dupeInstance = (DefaultInstance) objectIn.readObject();
        objectIn.close();

        return dupeInstance;
    }

    private void writeObject(java.io.ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        try
        {
            this.workflow = Engine.getThreadEngine().getWorkflow( this.workflowId );
        }
        catch (NoSuchWorkflowException e)
        {
            e.printStackTrace();
        }
    }

    public void enqueue(Path path)
    {
        this.queue.add( path );
    }

    public void dequeue(Path path)
    {
        this.queue.remove( path );
    }

    public Path[] getQueue()
    {
        return (Path[]) this.queue.toArray( new Path[ this.queue.size() ] );
    }

    public String toString()
    {
        return "[" + getId() + "| " + this.queue + "]";
    }
}
