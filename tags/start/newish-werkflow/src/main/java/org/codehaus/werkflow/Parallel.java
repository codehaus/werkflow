package org.codehaus.werkflow;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Parallel
    implements AsyncComponent
{
    private static final Component[] EMPTY_COMPONENT_ARRAY = new Component[0];

    private List branches;

    public Parallel()
    {
        this.branches = new ArrayList();
    }

    public Parallel(Component[] branches)
    {
        this();
        setBranches( branches );
    }

    public Parallel addBranch(Component branch)
    {
        this.branches.add( branch );

        return this;
    }

    public void setBranches(Component[] branches)
    {
        this.branches.clear();

        for ( int i = 0 ; i < branches.length ; ++i )
        {
            this.branches.add( branches[ i ] );
        }
    }

    public Component[] getBranches()
    {
        return (Component[]) this.branches.toArray( EMPTY_COMPONENT_ARRAY );
    }

    public Path[] begin(Instance instance,
                        Path thisPath)
    {
        int numBranches = getBranches().length;

        if ( numBranches > 0 )
        {
            Path[] nextPaths = new Path[ numBranches ];
            
            for ( int i = 0 ; i < numBranches ; ++i )
            {
                nextPaths[ i ] = thisPath.childPath( i );
            }
            
            return nextPaths;
        }

        return null;
    }

    public Path endChild(Instance instance,
                         Path path)
    {
        if ( instance.getActiveChildren( path.parentPath() ).length > 1 )
        {
            return DEFER;
        }

        return NONE;
    }

    public Component[] getChildren()
    {
        return getBranches();
    }
}
