package org.codehaus.werkflow.drools;

import org.codehaus.werkflow.Context;
import org.codehaus.werkflow.spi.SatisfactionManager;
import org.codehaus.werkflow.spi.SatisfactionCallback;

import org.drools.FactHandle;
import org.drools.WorkingMemory;
import org.drools.RuleBase;
import org.drools.FactException;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class DroolsSatisfactionManager
    implements SatisfactionManager
{
    public static final String APPDATA_KEY = "drools.satisfaction.manager";

    private Map satisfactions;
    private Map callbacks;

    private WorkingMemory workingMemory;

    public DroolsSatisfactionManager(RuleBase ruleBase)
    {
        this.satisfactions = new HashMap();
        this.callbacks     = new HashMap();
        this.workingMemory = ruleBase.newWorkingMemory();
        this.workingMemory.getApplicationDataMap().put( APPDATA_KEY,
                                                        this );
    }

    protected  WorkingMemory getWorkingMemory()
    {
        return this.workingMemory;
    }

    public boolean isSatisfied(String satisfactionId,
                               Context context)
    {
        return isSatisfied( satisfactionId,
                            context.getId() );
    }

    protected synchronized boolean isSatisfied(String satisfactionId,
                                               String contextId)
    {
        Set contextIds = (Set) this.satisfactions.get( satisfactionId );

        if ( contextIds != null )
        {
            return contextIds.contains( contextId );
        }

        return false;
    }

    public synchronized void notifySatisfied(String satisfactionId,
                                             String contextId)
    {
        Set contextIds = (Set) this.satisfactions.get( satisfactionId );
        
        if ( contextIds == null )
        {
            contextIds = new HashSet();
            this.satisfactions.put( satisfactionId,
                                    contextIds );
        }

        contextIds.add( contextId );

        Map callbacks = (Map) this.callbacks.get( satisfactionId );

        if ( callbacks != null )
        {
            SatisfactionCallback callback = (SatisfactionCallback) callbacks.get( contextId );

            if ( callback != null )
            {
                callback.notifySatisfied();
            }
        }
    }
    
    public synchronized boolean isSatisfied(String satisfactionId,
                                            Context context,
                                            SatisfactionCallback callback)
    {
        if ( isSatisfied( satisfactionId,
                          context ) )
        {
            return true;
        }

        try
        {
            getWorkingMemory().assertObject( setUpRequest( context ) );
            getWorkingMemory().fireAllRules();
            
            boolean isSatisfied = isSatisfied( satisfactionId,
                                               context.getId() );

            if ( ! isSatisfied )
            {
                Map callbacks = (Map) this.callbacks.get( satisfactionId );
                
                if ( callbacks == null )
                {
                    callbacks = new HashMap();
                    this.callbacks.put( satisfactionId,
                                        callbacks );
                }
                
                callbacks.put( context.getId(),
                               callback );
            }

            return isSatisfied;
        }
        catch (FactException e)
        {
            // TODO: set error flag
            return false;
        }
    }

    protected Request setUpRequest(Context context)
    {
        return new ContextRequest( context );
    }
}
