package org.codehaus.werkflow.drools;

import org.codehaus.werkflow.Context;
import org.codehaus.werkflow.Engine;
import org.codehaus.werkflow.NoSuchInstanceException;
import org.codehaus.werkflow.Transaction;
import org.codehaus.werkflow.helpers.SimpleSatisfactionManager;
import org.codehaus.werkflow.spi.DefaultSatisfactionValues;
import org.codehaus.werkflow.spi.RobustTransaction;
import org.codehaus.werkflow.spi.SatisfactionCallback;
import org.codehaus.werkflow.spi.SatisfactionManager;

import org.drools.WorkingMemory;
import org.drools.RuleBase;
import org.drools.FactException;

public class DroolsSatisfactionManager
    extends SimpleSatisfactionManager
    implements SatisfactionManager
{
    public static final String APPDATA_KEY = "drools.satisfaction.manager";

    private static final ThreadLocal threadTransaction = new ThreadLocal();

    private Engine engine;

    private WorkingMemory workingMemory;

    public DroolsSatisfactionManager(RuleBase ruleBase,
                                     Engine engine)
    {
        this.engine = engine;
        this.workingMemory = ruleBase.newWorkingMemory();
        this.workingMemory.getApplicationDataMap().put( APPDATA_KEY,
                                                        this );
    }

    protected WorkingMemory getWorkingMemory()
    {
        return this.workingMemory;
    }

    public synchronized boolean isSatisfied(RobustTransaction transaction,
                                            String satisfactionId,
                                            Context context,
                                            SatisfactionCallback callback)
    {
        threadTransaction.set( transaction );

        boolean isSatisfied = super.isSatisfied( transaction,
                                                 satisfactionId,
                                                 context,
                                                 callback );

        threadTransaction.set( null );

        return isSatisfied;
    }

    protected void provokeSatisfactions(String satisfactionId,
                                        Context context)
    {
        try
        {
            getWorkingMemory().assertObject( setUpRequest( context ) );
            getWorkingMemory().fireAllRules();
        }
        catch (FactException e)
        {
            // TODO: set error flag
        }
    }

    protected Request setUpRequest(Context context)
    {
        return new ContextRequest( context );
    }

    public void notifySatisfied(String satisfactionId,
                                String contextId)
        throws NoSuchInstanceException, InterruptedException, Exception
    {
        Transaction tx = (Transaction) threadTransaction.get();

        boolean localTransaction = false;

        if ( tx == null )
        {
            localTransaction = true;

            tx = engine.beginTransaction( contextId );
        }

        tx.satisfy( satisfactionId,
                    new DefaultSatisfactionValues() );

        if ( localTransaction )
        {
            tx.commit();
        }
    }
}
