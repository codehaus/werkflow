package com.werken.werkflow.core;

import com.werken.werkflow.AttributeDeclaration;
import com.werken.werkflow.Attributes;
import com.werken.werkflow.InvalidAttributesException;
import com.werken.werkflow.NoSuchCaseException;
import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.ProcessNotCallableException;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.Waiter;
import com.werken.werkflow.definition.petri.Arc;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.messaging.IncompatibleMessageSelectorException;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.MessageSink;
import com.werken.werkflow.service.messaging.MessagingManager;
import com.werken.werkflow.service.messaging.NoSuchMessageException;
import com.werken.werkflow.service.persistence.PersistenceException;
import com.werken.werkflow.service.persistence.ProcessPersistenceManager;

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
        this.evaluator          = new Evaluator( this );
        this.messageHandler     = new MessageHandler( messagingManager,
                                                     this );

    }

    void initialize()
        throws Exception
    {
        initializeInitiators();
        initializeMessageWaiters();
    }

    private void initializeInitiators()
        throws IncompatibleMessageSelectorException
    {
        if ( isCallable() )
        {
            return;
        }

        Transition[] transitions = getProcessDefinition().getNet().getTransitions();

        for ( int i = 0 ; i < transitions.length ; ++i )
        {
            initializeMessageInitiator( transitions[i] );
        }
    }

    private void initializeMessageWaiters()
        throws IncompatibleMessageSelectorException
    {
        Transition[] transitions = getProcessDefinition().getNet().getTransitions();

        for ( int i = 0 ; i < transitions.length ; ++i )
        {
            initializeMessageWaiter( transitions[i] );
        }
    }
    private void initializeMessageInitiator(Transition transition)
        throws IncompatibleMessageSelectorException
    {
        if ( isAttachedToIn( transition ) )
        {
            Waiter waiter = transition.getWaiter();

            if ( waiter instanceof MessageWaiter )
            {
                getMessageHandler().add( transition,
                                         this );
            }
        }
    }

    private void initializeMessageWaiter(Transition transition)
        throws IncompatibleMessageSelectorException
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

        this.messageHandler.add( transition,
                                 null );
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

    public CoreWorkItem[] evaluate(CoreChangeSet changeSet,
                                   CoreProcessCase processCase)
    {
        return getEvaluator().evaluate( changeSet,
                                        processCase );
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
        System.err.println( "acceptMessage; " + message.getMessage() );

        getMessageHandler().acceptMessage( message );

        // getScheduler().schedule( (CoreProcessCase[]) schedCases.toArray( CoreProcessCase.EMPTY_ARRAY ) );
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


    ProcessCase initiate(Transition transition,
                         String messageId)
        throws PersistenceException
    {
        CoreChangeSet changeSet = newChangeSet();

        CoreProcessCase newCase = getCaseManager().newCase( Attributes.EMPTY_ATTRIBUTES );

        newCase.addToken( "in" );

        CoreWorkItem workItem = new CoreWorkItem( newCase,
                                                  transition,
                                                  new String[] { "in" },
                                                  messageId );

        changeSet.addModifiedCase( newCase );

        try
        {
            CoreActivity activity = workItem.satisfy( changeSet );

            getScheduler().getExecutor().enqueueActivity( activity );

            changeSet.commit();

            return newCase;
        }
        catch (InterruptedException e)
        {
            // swallow, don't commit changeset.
        }

        return null;
    }

    private void validateAttributes(Attributes attributes)
        throws InvalidAttributesException
    {
        // FIXME
    }

    ProcessCase getProcessCase(String caseId)
        throws NoSuchCaseException, PersistenceException
    {
        return getCaseManager().getCase( caseId );
    }
}
