package com.werken.werkflow.task;

import com.werken.werkflow.action.Action;
import com.werken.werkflow.resource.Resource;

public interface Task
{
    final static Task[] EMPTY_ARRAY = new Task[0];

    ResourceSpec[] getResourceSpecs();

    Action getAction();
}
