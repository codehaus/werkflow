package org.codehaus.werkflow;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import java.io.Serializable;

class Scope
    implements Serializable
{
    private static final Scope[] EMPTY_SCOPE_ARRAY = new Scope[ 0 ];

    private int segment;
    private Scope parent;
    private Set children;

    Scope(int segment)
    {
        this( null,
              segment );
    }

    private Scope(Scope parent,
                  int segment)
    {
        this.parent = parent;
        this.segment = segment;
        this.children = new HashSet();
    }

    Scope getParent()
    {
        return this.parent;
    }

    Scope newChild(int segment)
    {
        Scope[] children = getChildren();

        for ( int i = 0 ; i < children.length ; ++i )
        {
            if ( children[ i ].getSegment() == segment )
            {
                throw new AssumptionViolationError( "child of <" + segment +"> already exists in " + this + " :: " + this.children );
            }
        }

        Scope child = new Scope( this,
                                 segment );

        this.children.add( child );

        return child;
    }

    Scope[] newChildren(int[] segments)
    {
        Scope[] children = getChildren();

        for ( int i = 0 ; i < children.length ; ++i )
        {
            if ( children[ i ].getSegment() == segment )
            {
                throw new AssumptionViolationError( "child of <" + segment +"> already exists in " + this + " :: " + this.children );
            }
        }

        for ( int i = 0 ; i < segments.length ; ++i )
        {
            Scope child = new Scope( segments[ i ] );
            
            this.children.add( child );
        }

        return getChildren();
    }

    void removeChild(int segment)
    {
        Scope child = getChild( segment );

        this.children.remove( child );
    }

    int getSegment()
    {
        return this.segment;
    }

    Scope[] getChildren()
    {
        return (Scope[]) this.children.toArray( EMPTY_SCOPE_ARRAY );
    }

    Scope getChild(int segment)
    {
        Scope[] children = getChildren();

        for ( int i = 0 ; i < children.length ; ++i )
        {
            if ( children[ i ].getSegment() == segment )
            {
                return children[ i ];
            }
        }

        throw new AssumptionViolationError( "no child <" + segment + ">" );
    }

    public String toString()
    {
        if ( getParent() != null )
        {
            return getParent() + "." + getSegment();
        }

        return "" + getSegment();
    }
}
