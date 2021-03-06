package org.codehaus.werkflow;

import org.codehaus.werkflow.spi.Instance;
import org.codehaus.werkflow.spi.Path;
import org.codehaus.werkflow.spi.Component;
import org.codehaus.werkflow.spi.AsyncComponent;

import java.util.List;
import java.util.ArrayList;

public class MockAsyncComponent
    implements AsyncComponent
{
    private List children;

    public MockAsyncComponent()
    {
        this.children = new ArrayList();
    }

    public void addChild(Component child)
    {
        this.children.add( child );
    }

    public Component[] getChildren()
    {
        return (Component[]) this.children.toArray( new Component[ this.children.size() ] );
    }

    public Path[] begin(Instance instance,
                        Path thisPath)
    {
        return null;
    }

    public Path endChild(Instance instance,
                         Path childPath)
    {
        return null;
    }
}

