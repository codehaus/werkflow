package org.codehaus.werkflow.helpers;

import org.codehaus.werkflow.spi.PersistenceManager;

public class SimplePersistenceManager implements PersistenceManager
{
    boolean active;

    public SimplePersistenceManager()
    {
        active = false;
    }

    public void beginTransaction()
    {
        active = true;
    }

    public void commitTransaction()
    {
        active = false;
    }

    public boolean isTransactionActive()
    {
        return active;
    }

    public void rollbackTransaction()
    {
        active = false;
    }

}
