package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.InitialContext;
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

    void startTransaction(RobustInstance instance)
        throws Exception;

    void commitTransaction(RobustInstance instance)
        throws Exception;

    void abortTransaction(RobustInstance instance)
        throws Exception;

}
