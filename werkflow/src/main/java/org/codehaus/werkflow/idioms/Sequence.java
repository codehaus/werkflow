package org.codehaus.werkflow.idioms;

import org.codehaus.werkflow.spi.Instance;
import org.codehaus.werkflow.spi.Component;
import org.codehaus.werkflow.spi.AsyncComponent;
import org.codehaus.werkflow.spi.Path;

import java.util.List;
import java.util.ArrayList;

public class Sequence
    implements AsyncComponent
{
    private static final int[] FIRST = new int[] { 0 };

    private static final Component[] EMPTY_COMPONENT_ARRAY = new Component[0];

    private List steps;

    public Sequence()
    {
        this.steps = new ArrayList();
    }
    public Sequence(Component[] step)
    {
        this();
        setSteps( step );
    }

    public Sequence addStep(Component step)
    {
        this.steps.add( step );

        return this;
    }

    public void setSteps(Component[] steps)
    {
        this.steps.clear();

        for ( int i = 0 ; i < steps.length ; ++i )
        {
            this.steps.add( steps[ i ] );
        }
    }

    public Component[] getSteps()
    {
        return (Component[]) this.steps.toArray( EMPTY_COMPONENT_ARRAY );
    }

    public Path[] begin(Instance instance,
                        Path thisPath)
    {
        if ( this.steps.isEmpty() )
        {
            return null;
        }

        return new Path[] { thisPath.childPath( 0 ) } ;
    }

    public Path endChild(Instance instance,
                         Path childPath)
    {
        int tail = childPath.tail();

        if ( tail < steps.size() - 1 )
        {
            return childPath.nextSiblingPath();
        }

        return NONE;
    }

    public Component[] getChildren()
    {
        return getSteps();
    }
}
