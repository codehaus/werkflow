package org.codehaus.werkflow.simple;

import org.codehaus.werkflow.spi.Instance;

import java.util.Properties;

public interface ActionManager
{
    public void perform(String actionId,
                        Instance instance,
                        Properties properties)
        throws Exception;
}
