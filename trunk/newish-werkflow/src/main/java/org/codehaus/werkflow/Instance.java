package org.codehaus.werkflow;

import org.codehaus.werkflow.spi.Path;

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

    boolean isComplete();

    void waitFor()
        throws InterruptedException;
}
