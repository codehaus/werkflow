package org.codehaus.werkflow.spi;

public interface PersistenceManager
{
    void beginTransaction();
    void commitTransaction();
    boolean isTransactionActive();
    void rollbackTransaction();
}
