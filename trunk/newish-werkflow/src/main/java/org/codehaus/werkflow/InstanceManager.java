package org.codehaus.werkflow;

public interface InstanceManager
{
    RobustInstance newInstance(Workflow workflow,
                               String id,
                               Context initialContext)
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
