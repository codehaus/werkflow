package org.codehaus.werkflow;

import org.codehaus.werkflow.spi.*;

class InstanceTask
{
    private RobustInstance instance;
    private Path path;

    InstanceTask(RobustInstance instance,
                 Path path)
    {
        this.instance = instance;
        this.path     = path;
    }

    RobustInstance getInstance()
    {
        return this.instance;
    }

    Path getPath()
    {
        return this.path;
    }
}
