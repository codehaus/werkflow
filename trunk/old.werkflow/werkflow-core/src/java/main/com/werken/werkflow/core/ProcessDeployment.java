package com.werken.werkflow.core;

import com.werken.werkflow.Attributes;
import com.werken.werkflow.AttributeDeclaration;
import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.InvalidAttributesException;
import com.werken.werkflow.NoSuchCaseException;
import com.werken.werkflow.ProcessNotCallableException;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.Waiter;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.petri.Arc;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.MessageSink;
import com.werken.werkflow.service.messaging.MessagingManager;
import com.werken.werkflow.service.messaging.NoSuchMessageException;
import com.werken.werkflow.service.persistence.CaseTransfer;
import com.werken.werkflow.service.persistence.ProcessPersistenceManager;
import com.werken.werkflow.service.persistence.PersistenceException;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;

class ProcessDeployment
    implements ProcessInfo, CaseEvaluator, ChangeSetSource, MessageSink, MessageConsumer
{
    private ProcessDefinition processDefinition;
    private MessageHandler messageHandler;
    private CaseManager caseManager;
    private Scheduler scheduler;
    private Evaluator evaluator;
    private ProcessPersistenceManager persistenceManager;

    private long counter;

    ProcessDeployment(ProcessDefinition processDefinition,
                      Scheduler scheduler,
                      ProcessPersistenceManager persistenceManager,
                      MessagingManager messagingManager)
    {
        this.processDefinition  = processDefinition;

        this.caseManager        = new CaseManager( this,
                                                   this,
                                                   this,
                                                   this,
                                                   persistenceManager );

        this.persistenceManager = persistenceManager;

        this.scheduler          = scheduler;
        this.evaluator          = new Evaluator( processDefinition );
        this.messageHandler     = new MessageHandler( messagingManager,
                                                     this );

    }

    void initialize()
    {
        initializeInitiators();
        initializeMessageWaiters();
    }

    private void initializeInitiators()
    {
        if ( isCallable() )
        {
            return;
        }
    }

    private void initializeMessageWaiters()
    {
        Transition[] transitions = getProcessDefinition().getNet().getTransitions();

        for ( int i = 0 ; i < transitions.length ; ++i )
        {
            initializeMessageWaiter( transitions[i] );
        }
    }

    private void initializeMessageWaiter(Transition transition)
    {
        Waiter waiter = transition.getWaiter();

        if ( ! ( waiter instanceof MessageWaiter ) )
        {
            return;
        }

        // If the process is not callable, then any transition
        // connected to (in) will be dealt with as an initiation
        // waiter instead of a correlation waiter.
        
        if ( ! isCallable()
             &&
             isAttachedToIn( transition ) )
        {
            return;
        }

        MessageWaiter msgWaiter = (MessageWaiter) waiter;

        MessageType type = msgWaiter.getMessageType();
    }

    private boolean isAttachedToIn(Transition transition)
    {
        Arc[] arcs = transition.getArcsFromPlaces();

        for ( int i = 0 ; i < arcs.length ; ++i )
        {
            if ( arcs[i].getPlace().equals( "in" ) )
            {
                return true;
            }
        }

        return false;
    }

    private boolean isCallable()
    {
        return ( getProcessDefinition().getInitiationType() 
                 ==
                 ProcessDefinition.InitiationType.CALL );
    }

    Evaluator getEvaluator()
    {
        return this.evaluator;
    }

    public CoreWorkItem[] evaluate(CoreProcessCase processCase)
    {
        return getEvaluator().evaluate( processCase );
    }

    public String getPackageId()
    {
        return getProcessDefinition().getPackageId();
    }

    public String getId()
    {
        return getProcessDefinition().getId();
    }

    public AttributeDeclaration[] getAttributeDeclarations()
    {
        return getProcessDefinition().getAttributeDeclarations();
    }

    public AttributeDeclaration getAttributeDeclaration(String id)
    {
        return getProcessDefinition().getAttributeDeclaration( id );
    }

    public String getDocumentation()
    {
        return getProcessDefinition().getDocumentation();
    }

    ProcessDefinition getProcessDefinition()
    {
        return this.processDefinition;
    }

    MessageHandler getMessageHandler()
    {
        return this.messageHandler;
    }

    public CoreChangeSet newChangeSet()
    {
        return new CoreChangeSet( this );
    }

    public void commit(CoreChangeSet changeSet)
        throws PersistenceException
    {
        getPersistenceManager().persist( changeSet );

        CoreProcessCase[] modifiedCases = changeSet.getCoreModifiedCases();

        try
        {
            getScheduler().schedule( modifiedCases );
        }
        catch (InterruptedException e)
        {
            // swallow
        }
    }

    private ProcessPersistenceManager getPersistenceManager()
    {
        return this.persistenceManager;
    }

    public void acceptMessage(Message message)
    {
        CoreChangeSet changeSet = newChangeSet();

        getMessageHandler().acceptMessage( changeSet,
                                           message );

        try
        {
            changeSet.commit();
        }
        catch (PersistenceException e)
        {
            e.printStackTrace();
        }

        Correlation[]   correlations = changeSet.getCoreCorrelations();
        CoreProcessCase processCase  = null;

        Set schedCases = new HashSet();

        for ( int i = 0 ; i < correlations.length ; ++i )
        {
            try
            {
                processCase = getCaseManager().getCase( correlations[i].getCaseId() );
                
                processCase.addCorrelation( correlations[i] );

                schedCases.add( processCase );
            }
            catch (NoSuchCaseException e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            getScheduler().schedule( (CoreProcessCase[]) schedCases.toArray( CoreProcessCase.EMPTY_ARRAY ) );
        }
        catch (InterruptedException e)
        {
            // swallow
        }
    }

    /** @see MessageConsumer
     */
    public Message consumeMessage(CoreChangeSet changeSet,
                                  CoreProcessCase processCase,
                                  String transitionId,
                                  String messageId)
        throws NoSuchMessageException
    {
        return getMessageHandler().consumeMessage( changeSet,
                                                   processCase,
                                                   transitionId,
                                                   messageId );
    }

    Scheduler getScheduler()
    {
        return this.scheduler;
    }

    CaseManager getCaseManager()
    {
        return this.caseManager;
    }

    ProcessCase call(Attributes attributes)
        throws InvalidAttributesException, ProcessNotCallableException, PersistenceException
    {
        if ( ! isCallable() )
        {
            throw new ProcessNotCallableException( getPackageId(),
                                                   getId() );
        }

        validateAttributes( attributes );

        CoreChangeSet changeSet = newChangeSet();

        CoreProcessCase newCase = getCaseManager().newCase( attributes );

        newCase.addToken( "in" );

        changeSet.addModifiedCase( newCase );

        changeSet.commit();

        return newCase;
    }

    private void validateAttributes(Attributes attributes)
        throws InvalidAttributesException
    {
        // FIXME
    }

    ProcessCase getProcessCase(String caseId)
        throws NoSuchCaseException
    {
        return getCaseManager().getCase( caseId );
    }
}
