package com.werken.werkflow.task;

import com.werken.werkflow.action.Action;

public class DefaultTask
    implements Task
{
    private Action action;

    public DefaultTask()
    {
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
