package com.werken.werkflow.task;

import com.werken.werkflow.action.Action;

public interface Task
{
    final static Task[] EMPTY_ARRAY = new Task[0];

    Action getAction();
}
