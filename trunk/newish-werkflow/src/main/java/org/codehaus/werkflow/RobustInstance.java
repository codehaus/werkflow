package org.codehaus.werkflow;

import java.util.Map;
import java.util.HashMap;

public interface RobustInstance
    extends Instance 
{
    Path[] getActiveChildren(Path path);

    void push(Path path);

    void push(Path[] paths);

    void pop(Path path);

    void setComplete(boolean complete);

    Scope getScope(Path path);
}
