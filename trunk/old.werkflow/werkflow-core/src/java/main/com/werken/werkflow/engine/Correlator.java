package com.werken.werkflow.engine;

import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.MessageCorrelator;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.service.messaging.Registration;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class Correlator
{
    private WorkflowEngine engine;

    private MessageType messageType;

    private List messages;
    private List blockers;

    private Map messageWaiters;

    public Correlator(WorkflowEngine engine,
                      MessageType messageType)
    {
        this.engine      = engine;
        this.messageType = messageType;

        this.messages = new LinkedList();
        this.blockers = new LinkedList();

        this.messageWaiters = new HashMap();
    }

    public WorkflowEngine getEngine()
    {
        return this.engine;
    }

    public MessageType getMessageType()
    {
        return this.messageType;
    }

    public void addMessage(Object message)
    {
        if ( ! checkCorrelationsForNewMessage( message ) )
        {
            this.messages.add( message );
        }
    }

    public void removeMessage(Object message)
    {
        while ( this.messages.remove( message ) )
        {
            // intentionally left blank
        }
    }

    public void addBlocker(String transitionId,
                           WorkflowProcessCase processCase)
    {
        if ( ! checkCorrelationsForNewBlocker( transitionId,
                                               processCase ) )
        {
            this.blockers.add( processCase.getId() );
        }
    }

    public void removeBlocker(String processCaseId)
    {
        while ( this.blockers.remove( processCaseId ) )
        {
            // intentionally left blank
        }
    }

    protected boolean checkCorrelationsForNewMessage(Object message)
    {
        return false;
    }

    protected boolean checkCorrelationsForNewBlocker(String transitionId,
                                                     WorkflowProcessCase processCase)
    {
        MessageWaiter     messageWaiter     = (MessageWaiter) this.messageWaiters.get( transitionId );
        MessageCorrelator messageCorrelator = messageWaiter.getMessageCorrelator();

        Iterator messageIter = this.messages.iterator();
        Object   eachMessage = null;


        while ( messageIter.hasNext() )
        {
            eachMessage = messageIter.next();

            try
            {
                if ( messageCorrelator.correlates( eachMessage,
                                                   processCase ) )
                {
                    // FIXME MATCH!

                    return true;
                }
            }
            catch (Exception e)
            {
                // FIXME
                e.printStackTrace();
            }
        }

        return false;
    }

    public void addMessageWaiter(String transitionId,
                                 MessageWaiter messageWaiter)
    {
        this.messageWaiters.put( transitionId,
                                 messageWaiter );
    }
}
