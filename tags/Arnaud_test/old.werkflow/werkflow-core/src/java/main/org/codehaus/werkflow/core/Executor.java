package org.codehaus.werkflow.core;

/*
 $Id$

 Copyright 2003 (C) The Codehaus. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "werkflow" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Codehaus.  For written permission,
    please contact info@codehaus.org.

 4. Products derived from this Software may not be called "werkflow"
    nor may "werkflow" appear in their names without prior written
    permission of The Codehaus. "werkflow" is a registered
    trademark of The Codehaus.

 5. Due credit should be given to The Codehaus -
    http://werkflow.codehaus.org/

 THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import org.codehaus.werkflow.Attributes;
import org.codehaus.werkflow.definition.petri.Arc;
import org.codehaus.werkflow.definition.petri.Place;
import org.codehaus.werkflow.definition.petri.Transition;
import org.codehaus.werkflow.expr.AttributesExpressionContext;
import org.codehaus.werkflow.expr.Expression;
import org.codehaus.werkflow.expr.ExpressionContext;
import org.codehaus.werkflow.service.persistence.PersistenceException;

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
