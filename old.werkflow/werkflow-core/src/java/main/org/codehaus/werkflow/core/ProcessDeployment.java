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

import org.codehaus.werkflow.AttributeDeclaration;
import org.codehaus.werkflow.Attributes;
import org.codehaus.werkflow.InvalidAttributesException;
import org.codehaus.werkflow.NoSuchCaseException;
import org.codehaus.werkflow.ProcessCase;
import org.codehaus.werkflow.ProcessInfo;
import org.codehaus.werkflow.ProcessNotCallableException;
import org.codehaus.werkflow.definition.MessageWaiter;
import org.codehaus.werkflow.definition.ProcessDefinition;
import org.codehaus.werkflow.definition.Waiter;
import org.codehaus.werkflow.definition.petri.Arc;
import org.codehaus.werkflow.definition.petri.Transition;
import org.codehaus.werkflow.service.messaging.IncompatibleMessageSelectorException;
import org.codehaus.werkflow.service.messaging.Message;
import org.codehaus.werkflow.service.messaging.MessageSink;
import org.codehaus.werkflow.service.messaging.MessagingManager;
import org.codehaus.werkflow.service.messaging.NoSuchMessageException;
import org.codehaus.werkflow.service.persistence.PersistenceException;
import org.codehaus.werkflow.service.persistence.ProcessPersistenceManager;

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
        getProcessDefinition().getNet().dump();
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

        if ( ( ! isCallable() )
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
            if ( arcs[i].getPlace().getId().equals( "in" ) )
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

    public void addCase(CoreProcessCase processCase,
                        String transitionId)
    {
        getMessageHandler().addCase( processCase,
                                     transitionId );
    }

    public void acceptMessage(Message message)
    {
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
