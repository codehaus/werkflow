package org.codehaus.werkflow.spi;

import java.io.Serializable;

public class Path
    implements Serializable
{
    private int[] segments;

    public Path()
    {
        this.segments = new int[] { 0 };
    }

    public Path(int[] segments)
    {
        this.segments = segments;
    }

    public int[] getSegments()
    {
        return this.segments;
    }

    public Path parentPath()
    {
        if ( this.segments.length == 1 )
        {
            return null;
        }

        return new Path( head() );
    }

    public Path nextSiblingPath()
    {
        int[] siblingSegments = new int[ this.segments.length ];

        for ( int i = 0 ; i < siblingSegments.length ; ++i )
        {
            siblingSegments[ i ] = this.segments[ i ];
        }

        siblingSegments[ siblingSegments.length - 1 ] = siblingSegments[ siblingSegments.length - 1 ] + 1;

        Path sibling = new Path( siblingSegments );

        return sibling;
    }

    public Path childPath(int childIndex)
    {
        int[] childSegments = new int[ this.segments.length + 1 ];

        for ( int i = 0 ;  i < this.segments.length ; ++i )
        {
            childSegments[ i ] = this.segments[ i ];
        }

        childSegments[ childSegments.length - 1 ] = childIndex;

        return new Path( childSegments );
    }

    int[] head()
    {
        int[] head = new int[ this.segments.length - 1 ];

        for ( int i = 0 ; i < head.length ; ++i )
        {
            head[ i ] = this.segments[ i ];
        }

        return head;
    }

    public int tail()
    {
        return this.segments[ this.segments.length - 1 ];
    }

    public boolean equals(Object thatObj)
    {
        Path that = (Path) thatObj;

        if ( this.segments.length != that.segments.length )
        {
            return false;
        }
        else
        {
            for ( int i = 0 ; i < this.segments.length ; ++i )
            {
                if ( this.segments[ i ] != that.segments[ i ] )
                {
                    return false;
                }
            }
        }

        return true;
    }

    public int hashCode()
    {
        return toString().hashCode();
    }

    public String toString()
    {
        StringBuffer text = new StringBuffer();

        for ( int i = 0 ; i < this.segments.length ; ++i )
        {
            if ( i > 0 )
            {
                text.append( '.' );
            }
            text.append( this.segments[ i ] );
        }

        return text.toString();
    }
}

