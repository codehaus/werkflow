package com.werken.werkflow.engine;

import com.werken.werkflow.NoSuchCaseException;
import com.werken.werkflow.NoSuchProcessException;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.NoSuchMessageException;

import java.util.List;
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

        Iterator caseIdIter = this.caseIds.iterator();
        String   eachCaseId = null;
        WorkflowProcessCase eachCase = null;

        while ( caseIdIter.hasNext() )
        {
            eachCaseId = (String) caseIdIter.next();

            try
            {
                eachCase = getEngine().getProcessCase( eachCaseId );

                attemptCorrelation( message,
                                    eachCase );
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
    }

    void removeMessage(String messageId)
    {
        while ( this.messageIds.remove( messageId ) )
        {
            // intentionally left blank
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
    }

    void addProcessCase(String processCaseId)
    {
        this.caseIds.add( processCaseId );
    }

    boolean containsProcessCase(String processCaseId)
    {
        return this.caseIds.contains( processCaseId );
    }

    void attemptCorrelation(Message message,
                            WorkflowProcessCase processCase)
    {
        try
        {
            if ( getMessageWaiter().getMessageCorrelator().correlates( message,
                                                                       processCase ) )
            {
                notifyCorrelation( message,
                                   processCase );
            }
        }
        catch (Exception e)
        {
            // FIXME
            e.printStackTrace();
        }
    }

    void notifyCorrelation(Message message,
                           WorkflowProcessCase processCase)
    {
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
    }

    boolean isCorrelated(String processCaseId)
    {
        return false;
    }
}
