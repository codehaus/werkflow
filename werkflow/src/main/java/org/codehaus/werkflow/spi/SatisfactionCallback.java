package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.Transaction;

public interface SatisfactionCallback
{
    void notifySatisfied(Transaction transaction)
        throws Exception;
}
