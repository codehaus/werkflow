package com.werken.werkflow.core;

import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import com.werken.werkflow.Attributes;
import com.werken.werkflow.definition.petri.Arc;
import com.werken.werkflow.definition.petri.Place;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.expr.AttributesExpressionContext;
import com.werken.werkflow.expr.Expression;
import com.werken.werkflow.expr.ExpressionContext;
import com.werken.werkflow.service.persistence.PersistenceException;

class Executor
    implements ActivityCompleter
{
    private PooledExecutor pool;
    private LinkedQueue queue;

    Executor()
    {
        this.queue = new LinkedQueue();

        this.pool = new PooledExecutor( this.queue );
        this.pool.setMinimumPoolSize( 1 );
        this.pool.setMaximumPoolSize( 5 );
        this.pool.setKeepAliveTime( 5000 );

        this.pool.createThreads( 5 );
    }

    void initialize()
    {
    }

    void enqueueActivities( CoreActivity[] activities )
        throws InterruptedException
    {
        for ( int i = 0; i < activities.length; ++i )
        {
            enqueueActivity( activities[i] );
        }
    }

    void enqueueActivity( CoreActivity activity )
        throws InterruptedException
    {
        System.err.println( "enqueue: " + activity );
        enqueue( new ExecutionEnqueuement( this,
                                           activity ) );
    }

    void enqueue( ExecutionEnqueuement enqueuement )
        throws InterruptedException
    {
        this.pool.execute( enqueuement );
    }

    public void complete( CoreActivity activity,
                          Attributes caseAttrs )
    {
        CoreProcessCase processCase = activity.getWorkItem().getCase();

        synchronized ( processCase )
        {
            ChangeSetSource changeSetSource = processCase.getChangeSetSource();

            CoreChangeSet changeSet = changeSetSource.newChangeSet();

            Transition transition = activity.getWorkItem().getTransition();

            transferAttributes( changeSet,
                                processCase,
                                caseAttrs );

            produceTokens( changeSet,
                           processCase,
                           transition );

            try
            {
                changeSet.commit();
            }
            catch ( PersistenceException e )
            {
                e.printStackTrace();
            }
        }
    }

    void transferAttributes( CoreChangeSet changeSet,
                             CoreProcessCase processCase,
                             Attributes caseAttrs )
    {
        String[] attrNames = caseAttrs.getAttributeNames();

        for ( int i = 0; i < attrNames.length; ++i )
        {
            processCase.setAttribute( attrNames[i],
                                      caseAttrs.getAttribute( attrNames[i] ) );
        }

        changeSet.addModifiedCase( processCase );
    }

    void produceTokens( CoreChangeSet changeSet,
                        CoreProcessCase processCase,
                        Transition transition )
    {
        Arc[] arcs = transition.getArcsToPlaces();

        ExpressionContext context = new AttributesExpressionContext( processCase );

        for ( int i = 0; i < arcs.length; ++i )
        {
            Expression expr = arcs[i].getExpression();

            if ( expr != null )
            {
                try
                {
                    if ( !expr.evaluateAsBoolean( context ) )
                    {
                        continue;
                    }
                }
                catch ( Exception e )
                {
                    // INTERNAL ERROR
                    e.printStackTrace();
                }
            }

            Place place = arcs[i].getPlace();

            processCase.addToken( place.getId() );
        }

        changeSet.addModifiedCase( processCase );
    }

    public void completeWithError( CoreActivity activity,
                                   Throwable error )
    {
        CoreProcessCase processCase = activity.getWorkItem().getCase();

        synchronized ( processCase )
        {
            error.printStackTrace();
        }
    }
}
