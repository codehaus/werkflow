package org.codehaus.werkflow.simple;

import org.codehaus.werkflow.Instance;

public interface ActionManager
{
    public void perform(String actionId,
                        Instance instance)
        throws Exception;
}
