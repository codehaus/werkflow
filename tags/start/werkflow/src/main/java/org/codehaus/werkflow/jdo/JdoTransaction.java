package org.codehaus.werkflow.jdo;

import org.codehaus.werkflow.EngineTransaction;

import javax.jdo.PersistenceManager;

public class JdoTransaction
    extends EngineTransaction
{
    private static final ThreadLocal threadTransaction = new ThreadLocal();

    private PersistenceManager persistenceManager;

    public JdoTransaction(PersistenceManager persistenceManager,
                          JdoEngine engine,
                          String transactionId,
                          String instanceId)
    {
        super( engine,
               transactionId,
               instanceId );

        threadTransaction.set( this );
    }

    public PersistenceManager getPersistenceManager()
    {
        return this.persistenceManager;
    }

    public void commit()
    {
        try
        {
            super.commit();
        }
        finally
        {
            threadTransaction.set( null );
        }
    }

    public void rollback()
    {
        try
        {
            super.rollback();
        }
        finally
        {
            threadTransaction.set( null );
        }
    }

    public static JdoTransaction currentTransaction()
    {
        return (JdoTransaction) threadTransaction.get();
    }

}
