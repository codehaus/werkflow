package com.werken.werkflow.task;

import com.werken.werkflow.action.Action;

import java.util.List;
import java.util.ArrayList;

public class DefaultTask
    implements Task
{
    private Action action;
    private List resourceSpecs;

    public DefaultTask()
    {
        this.resourceSpecs = new ArrayList();
    }

    public void setAction(Action action)
    {
        this.action = action;
    }

    public Action getAction()
    {
        return this.action;
    }

    public ResourceSpec[] getResourceSpecs()
    {
        return (ResourceSpec[]) this.resourceSpecs.toArray( ResourceSpec.EMPTY_ARRAY );
    }
}
