package com.werken.werkflow.task;

import com.werken.werkflow.action.Action;

import java.util.Set;
import java.util.HashSet;

public class MockTask
    implements Task
{
    private String id;
    private Action action;

    public MockTask(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
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
