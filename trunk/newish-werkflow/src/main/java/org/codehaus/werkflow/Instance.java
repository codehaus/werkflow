package org.codehaus.werkflow;

import java.util.Map;
import java.util.HashMap;

public interface Instance
    extends Context 
{
    Workflow getWorkflow();

    String getId();

    void put(String id,
             Object value);

    Object get(String id);

    Path[] getActiveChildren(Path path);

    void waitFor()
        throws InterruptedException;
}
