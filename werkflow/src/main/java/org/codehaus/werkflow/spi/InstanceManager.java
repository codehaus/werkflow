package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.InitialContext;
import org.codehaus.werkflow.ReadOnlyInstance;
import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.NoSuchInstanceException;
import org.codehaus.werkflow.DuplicateInstanceException;

public interface InstanceManager
{
    RobustInstance newInstance(Workflow workflow,
                               String id,
                               InitialContext initialContext)
        throws DuplicateInstanceException, Exception;

    RobustInstance getInstance(String id)
        throws NoSuchInstanceException, Exception;

    boolean hasInstance(String id);

    ReadOnlyInstance getReadOnlyInstance(String instanceId)
        throws NoSuchInstanceException, Exception;

    ReadOnlyInstance[] getReadOnlyInstances(String workflowId)
        throws Exception;
}
