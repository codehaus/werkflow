package org.codehaus.werkflow;

import java.util.Map;
import java.util.HashMap;

public class InstanceRef
    implements Instance
{
    private Instance instance;

    public InstanceRef(Instance instance)
    {
        setInstance( instance );
    }

    public Instance getInstance()
    {
        return this.instance;
    }

    public void setInstance(Instance instance)
    {
        this.instance = instance;
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
}
