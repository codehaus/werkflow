package com.werken.werkflow.core;

import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.Attributes;
import com.werken.werkflow.RuntimeWerkflowException;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.persistence.CaseTransfer;
import com.werken.werkflow.service.persistence.ProcessPersistenceManager;
import com.werken.werkflow.service.persistence.PersistenceException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
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

    CoreWorkItem[] evaluate()
    {
        return getCaseEvaluator().evaluate( this );
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


