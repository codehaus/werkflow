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

import org.codehaus.werkflow.ProcessCase;
import org.codehaus.werkflow.ProcessInfo;
import org.codehaus.werkflow.Attributes;
import org.codehaus.werkflow.RuntimeWerkflowException;
import org.codehaus.werkflow.service.persistence.CaseTransfer;
import org.codehaus.werkflow.service.persistence.ProcessPersistenceManager;
import org.codehaus.werkflow.service.persistence.PersistenceException;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

class CoreProcessCase
    implements ProcessCase, Attributes
{
    static final CoreProcessCase[] EMPTY_ARRAY = new CoreProcessCase[0];

    private ProcessPersistenceManager persistenceManager;
    private ChangeSetSource changeSetSource;
    private ProcessInfo processInfo;
    private CaseEvaluator caseEvaluator;
    private MessageConsumer messageConsumer;
    private String id;

    private List correlations;
    private List schedActivities;

    private CaseState state;

    CoreProcessCase(ProcessPersistenceManager persistenceManager,
                    ChangeSetSource changeSetSource,
                    CaseEvaluator caseEvaluator,
                    MessageConsumer messageConsumer,
                    ProcessInfo processInfo,
                    String id)
    {
        this.persistenceManager = persistenceManager;
        this.changeSetSource    = changeSetSource;
        this.caseEvaluator      = caseEvaluator;
        this.messageConsumer    = messageConsumer;
        this.processInfo        = processInfo;
        this.id                 = id;

        this.correlations       = new ArrayList();
        this.schedActivities    = new ArrayList();

        this.state              = null;
    }

    CoreProcessCase(ProcessPersistenceManager persistenceManager,
                    ChangeSetSource changeSetSource,
                    CaseEvaluator caseEvaluator,
                    MessageConsumer messageConsumer,
                    ProcessInfo processInfo,
                    CaseTransfer caseTransfer)
    {
        this( persistenceManager,
              changeSetSource,
              caseEvaluator,
              messageConsumer,
              processInfo,
              caseTransfer.getCaseId() );

        this.state = new CaseState( caseTransfer );
    }

    public String getId()
    {
        return this.id;
    }

    public ProcessInfo getProcessInfo()
    {
        return this.processInfo;
    }

    ChangeSetSource getChangeSetSource()
    {
        return this.changeSetSource;
    }

    CaseEvaluator getCaseEvaluator()
    {
        return this.caseEvaluator;
    }

    MessageConsumer getMessageConsumer()
    {
        return this.messageConsumer;
    }

    void setAttribute(String name,
                      Object value)
    {
        getState().setAttribute( name,
                                 value );
    }

    public Object getAttribute(String name)
    {
        return getState().getAttribute( name );
    }

    public String[] getAttributeNames()
    {
        return getState().getAttributeNames();
    }

    public boolean hasAttribute(String name)
    {
        return getState().hasAttribute( name );
    }

    void addCorrelation(Correlation correlation)
    {
        if ( ! correlation.getCaseId().equals( getId() ) )
        {
            throw new ConsistencyException( "correlation for '"
                                            + correlation.getCaseId()
                                            + "' added to '"
                                            + getId() + "'" );
        }
        this.correlations.add( correlation );
    }

    Correlation[] getCorrelations()
    {
        return (Correlation[]) this.correlations.toArray( Correlation.EMPTY_ARRAY );
    }

    Correlation[] getCorrelations(String transitionId)
    {
        List results = new ArrayList();

        Iterator    corIter = this.correlations.iterator();
        Correlation eachCor = null;

        while ( corIter.hasNext() )
        {
            eachCor = (Correlation) corIter.next();

            if ( eachCor.getTransitionId().equals( transitionId ) )
            {
                results.add( eachCor );
            }
        }

        return (Correlation[]) results.toArray( Correlation.EMPTY_ARRAY );
    }

    void removeCorrelationsByTransition(String transitionId)
    {
        Iterator    corIter = this.correlations.iterator();
        Correlation eachCor = null;

        while ( corIter.hasNext() )
        {
            eachCor = (Correlation) corIter.next();

            if ( eachCor.getTransitionId().equals( transitionId ) )
            {
                corIter.remove();
            }
        }
    }

    void removeCorrelationsByMessage(String messageId)
    {
        Iterator    corIter = this.correlations.iterator();
        Correlation eachCor = null;

        while ( corIter.hasNext() )
        {
            eachCor = (Correlation) corIter.next();

            if ( eachCor.getMessageId().equals( messageId ) )
            {
                corIter.remove();
            }
        }
    }

    void consumeTokens(String[] tokens)
    {
        getState().consumeTokens( tokens );
    }

    String[] getTokens()
    {
        return getState().getTokens();
    }

    boolean hasTokens(String[] tokens)
    {
        return getState().hasTokens( tokens );
    }

    void addToken(String token)
    {
        getState().addToken( token );
    }

    CoreWorkItem[] evaluate(CoreChangeSet changeSet)
    {
        return getCaseEvaluator().evaluate( changeSet,
                                            this );
    }

    void addScheduledActivity(CoreActivity activity)
    {
        this.schedActivities.add( activity );
    }

    CoreActivity[] getScheduledActivities()
    {
        return (CoreActivity[]) this.schedActivities.toArray( CoreActivity.EMPTY_ARRAY );
    }

    private ProcessPersistenceManager getPersistenceManager()
    {
        return this.persistenceManager;
    }

    private CaseState getState()
    {
        ensureLoaded();
        return this.state;
    }

    void ensureLoaded()
    {
        try
        {
            if ( this.state == null )
            {
                this.state = new CaseState( getPersistenceManager().loadCase( getId() ) );
            }
        }
        catch (PersistenceException e)
        {
            throw new RuntimeWerkflowException( e );
        }
    }
}


