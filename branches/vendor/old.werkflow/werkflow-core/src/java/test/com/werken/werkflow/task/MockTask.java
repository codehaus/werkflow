package com.werken.werkflow.task;

import com.werken.werkflow.action.Action;

import java.util.Set;
import java.util.HashSet;

public class MockTask
    implements Task
{
    private String id;
    private Set resourceSpecs;
    private Action action;

    public MockTask(String id)
    {
        this.id = id;
        this.resourceSpecs = new HashSet();
    }

    public String getId()
    {
        return this.id;
    }

    public void addResourceSpec(ResourceSpec resourceSpec)
    {
        this.resourceSpecs.add( resourceSpec );
    }

    public ResourceSpec[] getResourceSpecs()
    {
        return (ResourceSpec[]) this.resourceSpecs.toArray( ResourceSpec.EMPTY_ARRAY );
    }

    public void setAction(Action action)
    {
        this.action = action;
    }

    public Action getAction()
    {
        return this.action;
    }
}
