package org.codehaus.werkflow;

import java.util.Map;
import java.util.HashMap;

public class RobustInstanceRef
    implements RobustInstance
{
    private RobustInstance instance;

    public RobustInstanceRef(RobustInstance instance)
    {
        setInstance( instance );
    }

    public void setInstance(RobustInstance instance)
    {
        this.instance = instance;
    }

    public RobustInstance getInstance()
    {
        return this.instance;
    }

    public Engine getEngine()
    {
        return getInstance().getEngine();
    }

    public Workflow getWorkflow()
    {
        return getInstance().getWorkflow();
    }

    public String getId()
    {
        return getInstance().getId();
    }
            
    public void put(String id,
                    Object value)
    {
        getInstance().put( id,
                           value );
    }

    public Object get(String id)
    {
        return getInstance().get( id );
    }

    public Path[] getActiveChildren(Path path)
    {
        return getInstance().getActiveChildren( path );
    }

    public void waitFor()
        throws InterruptedException
    {
        getInstance().waitFor();
    }

    public void push(Path path)
    {
        getInstance().push( path );
    }

    public void push(Path[] paths)
    {
        getInstance().push( paths );
    }

    public void pop(Path path)
    {
        getInstance().pop( path );
    }

    public void setComplete(boolean complete)
    {
        getInstance().setComplete( complete );
    }

    public Scope getScope(Path path)
    {
        return getInstance().getScope( path );
    }
}
