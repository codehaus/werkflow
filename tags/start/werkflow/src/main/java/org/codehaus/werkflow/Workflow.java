package org.codehaus.werkflow;

import org.codehaus.werkflow.spi.Instance;
import org.codehaus.werkflow.spi.Component;
import org.codehaus.werkflow.spi.AsyncComponent;
import org.codehaus.werkflow.spi.Satisfaction;
import org.codehaus.werkflow.spi.Path;

import java.util.LinkedList;

public class Workflow
    implements AsyncComponent
{
    private String id;
    private Component body;

    public Workflow(String id)
    {
        this.id = id;
    }

    public Workflow(String id,
                    Component body)
    {
        this( id );
        this.body = body;
    }

    public String getId()
    {
        return this.id;
    }

    public void setBody(Component body)
    {
        this.body = body;
    }

    public Component getBody()
    {
        return this.body;
    }

    public Path[] begin(Instance instance,
                        Path thisPath)
    {
        return new Path[]
            {
                thisPath.childPath( 0 )
            };
    }

    public Path endChild(Instance instance,
                         Path thisPath)
    {
        return NONE;
    }

    public Component[] getChildren()
    {
        if ( getBody() == null )
        {
            return new Component[ 0 ];
        }
        return new Component[] { getBody() };
    }

    public Component getComponent(Path path)
    {
        int[] segments = path.getSegments();

        if ( segments[ 0 ] != 0 )
        {
            throw new AssumptionViolationError( "path must start with <0>" );
        }

        Component cur = this;

        if ( segments.length > 1 )
        {
            for ( int i = 1 ; i < segments.length ; ++i )
            {
                if ( ! ( cur instanceof AsyncComponent ) )
                {
                    throw new AssumptionViolationError( "child [" + cur.getClass().getName() + "] has no async component: " + toString( segments, i ) + " of " + path );
                }

                Component[] children = ((AsyncComponent)cur).getChildren();

                if ( children.length < segments[ i ] )
                {
                    throw new AssumptionViolationError( "no child [" + segments[ i ] + "] at [" + toString( segments, i ) + "]" );
                }

                cur = null;

                cur = children[ segments[ i ] ];

                if ( cur == null )
                {
                    throw new AssumptionViolationError( "child is null at [" + toString( segments, i ) + "]" );
                }
            }
        }
            
        return cur;
    }

    public Path getSatisfactionPath(String id)
    {
        LinkedList pathStack = new LinkedList();

        Path curPath = new Path();

        pathStack.addLast( curPath );

        while ( ! pathStack.isEmpty() )
        {
            curPath = (Path) pathStack.removeFirst();

            Component cur = getComponent( curPath );

            if ( cur instanceof Satisfaction )
            {
                if ( ((Satisfaction)cur).getId().equals( id ) )
                {
                    return curPath;
                }
            }
            else if ( cur instanceof AsyncComponent )
            {
                int numChildren = ((AsyncComponent)cur).getChildren().length;
                
                for ( int i = 0 ; i < numChildren ; ++i )
                {
                    pathStack.addLast( curPath.childPath( i ) );
                }
            }
                
            


        }

        throw new AssumptionViolationError( "no satisfaction named <" + id + ">" );
    }

    protected String toString(int[] path)
    {
        return toString( path,
                         path.length );
    }

    protected String toString(int[] path,
                              int len)
    {
        StringBuffer buf = new StringBuffer();

        for ( int i = 0 ; i < len ; ++i )
        {
            if ( i > 0 )
            {
                buf.append( "." );
            }

            buf.append( path[ i ] );
        }

        return buf.toString();
    }
}
