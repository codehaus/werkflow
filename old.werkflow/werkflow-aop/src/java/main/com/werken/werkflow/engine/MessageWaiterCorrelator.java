package com.werken.werkflow.engine;

import com.werken.werkflow.NoSuchCaseException;
import com.werken.werkflow.NoSuchProcessException;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.NoSuchMessageException;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;

public class MessageWaiterCorrelator
{
    private WorkflowEngine engine;
    private MessageTypeCorrelator msgTypeCorrelator;
    private String transitionId;
    private MessageWaiter messageWaiter;

    private List messageIds;
    private List caseIds;
    private List correlations;

    public MessageWaiterCorrelator(WorkflowEngine engine,
                                   MessageTypeCorrelator msgTypeCorrelator,
                                   String transitionId,
                                   MessageWaiter messageWaiter)
    {
        this.engine            = engine;
        this.msgTypeCorrelator = msgTypeCorrelator;
        this.transitionId      = transitionId;
        this.messageWaiter     = messageWaiter;

        this.messageIds    = new LinkedList();
        this.caseIds       = new LinkedList();
        this.correlations  = new LinkedList();
    }

    public WorkflowEngine getEngine()
    {
        return this.engine;
    }

    public MessageTypeCorrelator getMessageTypeCorrelator()
    {
        return this.msgTypeCorrelator;
    }

    public String getTransitionId()
    {
        return this.transitionId;
    }

    public MessageWaiter getMessageWaiter()
    {
        return this.messageWaiter;
    }

    Message getMessage(String msgId)
        throws NoSuchMessageException
    {
        return getMessageTypeCorrelator().getMessage( msgId );
    }

    public void acceptMessage(Message message)
    {
        addMessage( message.getId() );

        List reevaluate = new ArrayList();

        Iterator caseIdIter = this.caseIds.iterator();
        String   eachCaseId = null;
        WorkflowProcessCase eachCase = null;

        while ( caseIdIter.hasNext() )
        {
            eachCaseId = (String) caseIdIter.next();

            try
            {
                eachCase = getEngine().getProcessCase( eachCaseId );

                if ( attemptCorrelation( message,
                                         eachCase ) )
                {
                    reevaluate.add( eachCase );
                }
            }
            catch (NoSuchCaseException e)
            {
                // FIXME
                e.printStackTrace();
            }
            catch (NoSuchProcessException e)
            {
                // FIXME
                e.printStackTrace();
            }
        }

        Iterator            reevalIter = reevaluate.iterator();
        WorkflowProcessCase eachReeval = null;

        while ( reevalIter.hasNext() )
        {
            eachReeval = (WorkflowProcessCase) reevalIter.next();

            try
            {
                getEngine().evaluateCase( eachReeval );
            }
            catch (NoSuchProcessException e)
            {
                // FIXME
                e.printStackTrace();
            }
        }
    }

    void removeMessage(String messageId)
    {
        while ( this.messageIds.remove( messageId ) )
        {
            // intentionally left blank
        }

        Iterator    correlationIter = this.correlations.iterator();
        Correlation eachCorrelation = null;

        while ( correlationIter.hasNext() )
        {
            eachCorrelation = (Correlation) correlationIter.next();

            if ( eachCorrelation.getMessageId().equals( messageId ) )
            {
                correlationIter.remove();
            }
        }
    }

    void addMessage(String messageId)
    {
        this.messageIds.add( messageId );
    }

    public void acceptProcessCase(WorkflowProcessCase processCase)
    {
        if ( containsProcessCase( processCase.getId() ) )
        {
            return;
        }

        addProcessCase( processCase.getId() );

        Iterator msgIdIter = this.messageIds.iterator();
        String   eachMsgId = null;

        Message eachMsg = null;

        while ( msgIdIter.hasNext() )
        {
            eachMsgId = (String) msgIdIter.next();

            try
            {
                eachMsg = getMessage( eachMsgId );

                attemptCorrelation( eachMsg,
                                    processCase );
            }
            catch (NoSuchMessageException e)
            {
                // FIXME
                e.printStackTrace();
            }
        }
    }

    void removeProcessCase(String processCaseId)
    {
        while ( this.caseIds.remove( processCaseId ) )
        {
            // intentionally left blank
        }

        Iterator    correlationIter = this.correlations.iterator();
        Correlation eachCorrelation = null;

        while ( correlationIter.hasNext() )
        {
            eachCorrelation = (Correlation) correlationIter.next();

            if ( eachCorrelation.getProcessCaseId().equals( processCaseId ) )
            {
                correlationIter.remove();
            }
        }
    }

    void addProcessCase(String processCaseId)
    {
        this.caseIds.add( processCaseId );
    }

    boolean containsProcessCase(String processCaseId)
    {
        return this.caseIds.contains( processCaseId );
    }

    boolean attemptCorrelation(Message message,
                               WorkflowProcessCase processCase)
    {
        try
        {
            if ( getMessageWaiter().getMessageCorrelator().correlates( message.getMessage(),
                                                                       processCase ) )
            {
                notifyCorrelation( message,
                                   processCase );

                return true;
            }
        }
        catch (Exception e)
        {
            // FIXME
            e.printStackTrace();
        }

        return false;
    }

    void notifyCorrelation(Message message,
                           WorkflowProcessCase processCase)
    {
        this.correlations.add( new Correlation( message.getId(),
                                                processCase.getId() ) );
    }

    void evaluateCase(WorkflowProcessCase processCase,
                      String[] transitionIds)
    {
        for ( int i = 0 ; i < transitionIds.length ; ++i )
        {
            if ( transitionIds[i].equals( this.transitionId ) )
            {
                acceptProcessCase( processCase );
                return;
            }
        }

        Transition[] enabledTransitions = processCase.getEnabledTransitions();

        for ( int i = 0 ; i < enabledTransitions.length ; ++i )
        {
            if ( enabledTransitions[i].getId().equals( this.transitionId ) )
            {
                return;
            }
        }

        // if not accepted, then it's not correlating or
        // correlatable to anything here, so remove it completely
        // and remove all correlations.

        String processCaseId = processCase.getId();

        removeProcessCase( processCaseId );
    }

    boolean isCorrelated(String processCaseId)
    {
        Iterator    correlationIter = this.correlations.iterator();
        Correlation eachCorrelation = null;

        while ( correlationIter.hasNext() )
        {
            eachCorrelation = (Correlation) correlationIter.next();

            if ( eachCorrelation.getProcessCaseId().equals( processCaseId ) )
            {
                return true;
            }
        }

        return false;
    }

    Object consumeMessage(String processCaseId)
        throws NoSuchCorrelationException
    {
        Iterator    correlationIter = this.correlations.iterator();
        Correlation eachCorrelation = null;

        while ( correlationIter.hasNext() )
        {
            eachCorrelation = (Correlation) correlationIter.next();

            if ( eachCorrelation.getProcessCaseId().equals( processCaseId ) )
            {
                try
                {
                    Message message = getMessage( eachCorrelation.getMessageId() );

                    removeMessage( message.getId() );

                    return message.getMessage();
                }
                catch (NoSuchMessageException e)
                {
                    // FIXME
                    e.printStackTrace();
                }
            }
        }

        throw new NoSuchCorrelationException( processCaseId,
                                              getTransitionId() );
    }
}
