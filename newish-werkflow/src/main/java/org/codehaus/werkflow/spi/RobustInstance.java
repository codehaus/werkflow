package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.Instance;

import java.util.Map;
import java.util.HashMap;

public interface RobustInstance
    extends Instance 
{
    Path[] getActiveChildren(Path path);

    void push(Path path);

    void push(Path[] paths);

    void pop(Path path);

    void enqueue(Path path);

    void dequeue(Path path);

    Path[] getQueue();

    void setComplete(boolean complete);

    Scope getScope(Path path);
}
