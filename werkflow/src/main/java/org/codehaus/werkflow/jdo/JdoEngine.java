package org.codehaus.werkflow.jdo;

import org.codehaus.werkflow.Engine;
import org.codehaus.werkflow.spi.RobustTransaction;

import javax.jdo.PersistenceManager;

public class JdoEngine
    extends Engine
{
    private static final ThreadLocal threadPersistenceManager = new ThreadLocal();

    public JdoEngine()
    {
        setInstanceManager( new JdoInstanceManager() );
    }

    protected synchronized RobustTransaction instantiateTransaction(String transactionId,
                                                                    String instanceId)
    {
        return new JdoTransaction( getThreadPersistenceManager(),
                                   this,
                                   transactionId,
                                   instanceId );
        
    }

    public static void setThreadPersistenceManager(PersistenceManager persistenceManager)
    {
        threadPersistenceManager.set( persistenceManager );
    }

    public static PersistenceManager getThreadPersistenceManager()
    {
        return (PersistenceManager) threadPersistenceManager.get();
    }

}
