package org.codehaus.werkflow;

import org.codehaus.werkflow.spi.*;

class InstanceTask
{
    private String instanceId;
    private Path path;

    InstanceTask(String instanceId,
                 Path path)
    {
        this.instanceId = instanceId;
        this.path     = path;
    }

    String getInstanceId()
    {
        return this.instanceId;
    }

    Path getPath()
    {
        return this.path;
    }

    public String toString()
    {
        return getInstanceId() + " // " + getPath();
    }
}
