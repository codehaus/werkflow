package org.codehaus.werkflow;

import org.codehaus.werkflow.spi.SatisfactionValues;

public interface Transaction
{
    String getId();

    String getInstanceId();

    void newInstance(String workflowId,
                     String instanceId,
                     InitialContext initialContext)
        throws NoSuchWorkflowException, DuplicateInstanceException, Exception;

    void satisfy(String satisfactionId,
                 SatisfactionValues value);

    boolean isOpen();
    boolean isClosed();

    void commit();

    void rollback();
}
