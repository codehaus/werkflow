package org.codehaus.werkflow;

import org.codehaus.werkflow.spi.Path;

public class InstanceTask
{
    private final String instanceId;
    private final Path path;

    public InstanceTask(String instanceId,
                 Path path)
    {
        this.instanceId = instanceId;
        this.path     = path;
    }

    public String getInstanceId()
    {
        return this.instanceId;
    }

    public Path getPath()
    {
        return this.path;
    }

    public String toString()
    {
        return getInstanceId() + " // " + getPath();
    }
}
