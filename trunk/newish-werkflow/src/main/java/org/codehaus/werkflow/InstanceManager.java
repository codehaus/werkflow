package org.codehaus.werkflow;

public interface InstanceManager
{
    RobustInstance newInstance(Engine engine,
                               Workflow workflow,
                               String id,
                               Context initialContext)
        throws DuplicateInstanceException;

    RobustInstance getInstance(String id)
        throws Exception;

    void startTransaction(RobustInstance instance)
        throws Exception;

    void commitTransaction(RobustInstance instance)
        throws Exception;

    void abortTransaction(RobustInstance instance)
        throws Exception;

}
