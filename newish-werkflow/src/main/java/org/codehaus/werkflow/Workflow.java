package org.codehaus.werkflow;

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

        Component cur = getBody();

        if ( cur == null )
        {
            throw new AssumptionViolationError( "no body" );
        }

        if ( segments.length > 1 )
        {
            for ( int i = 1 ; i < segments.length ; ++i )
            {
                if ( ! ( cur instanceof AsyncComponent ) )
                {
                    throw new AssumptionViolationError( "child has not async component" );
                }

                Component[] children = ((AsyncComponent)cur).getChildren();

                if ( children.length < segments[ i ] )
                {
                    StringBuffer segmentsText = new StringBuffer();

                    for ( int j = 0 ; j <= i ; ++j )
                    {
                        segmentsText.append( segments[ i ] );

                        if ( j < i )
                        {
                            segmentsText.append( "." );
                        }
                    }

                    throw new AssumptionViolationError( "no child <" + segments[ i ] + "> at <" + segmentsText + ">" );
                }

                cur = children[ segments[ i ] ];

                if ( cur == null )
                {
                    StringBuffer segmentsText = new StringBuffer();
                    
                    for ( int j = 0 ; j <= i ; ++j )
                    {
                        segmentsText.append( segments[ i ] );
                        
                        if ( j < i )
                        {
                            segmentsText.append( "." );
                        }
                    }

                    throw new AssumptionViolationError( "child is null at <" + segmentsText + ">" );
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
}
