package com.werken.werkflow.engine;

import com.werken.werkflow.NoSuchCaseException;
import com.werken.werkflow.NoSuchProcessException;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.MessageCorrelator;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.service.messaging.Registration;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class Correlator
{
    private WorkflowEngine engine;

    private MessageType messageType;

    private List messages;

    private Map blockers;

    private Map messageWaiters;

    public Correlator(WorkflowEngine engine,
                      MessageType messageType)
    {
        this.engine      = engine;
        this.messageType = messageType;

        this.messages = new LinkedList();
        this.blockers = new HashMap();

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
        // FIXME for presence and abort if found.

        if ( ! checkCorrelationsForNewBlocker( transitionId,
                                               processCase ) )
        {
            List waiterBlockers = (List) this.blockers.get( transitionId );

            if ( waiterBlockers == null )
            {
                waiterBlockers = new LinkedList();
                this.blockers.put( transitionId,
                                   waiterBlockers );
            }

            waiterBlockers.add( processCase.getId() );
        }
    }

    public void removeBlocker(String processCaseId)
    {
        // FIXME
    }

    protected boolean checkCorrelationsForNewMessage(Object message)
    {
        Set transIds = this.blockers.keySet();

        Iterator transIdIter = transIds.iterator();
        String   eachTransId = null;

        while ( transIdIter.hasNext() )
        {
            eachTransId = (String) transIdIter.next();

            MessageWaiter     messageWaiter     = (MessageWaiter) this.messageWaiters.get( eachTransId );
            MessageCorrelator messageCorrelator = messageWaiter.getMessageCorrelator();

            List waiterBlockers = (List) this.blockers.get( eachTransId );

            Iterator caseIdIter = waiterBlockers.iterator();

            while ( caseIdIter.hasNext() )
            {
                String caseId = (String) caseIdIter.next();

                try
                {
                    WorkflowProcessCase processCase = getEngine().getProcessCase( caseId );
                    
                    try
                    {
                        if ( messageCorrelator.correlates( message,
                                                           processCase ) )
                        {
                            // FIXME MATCH!
                            return true;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                catch (NoSuchCaseException e)
                {
                    // FIXME complain
                    e.printStackTrace();
                    continue;
                }
                catch (NoSuchProcessException e)
                {
                    // FIXME complain
                    e.printStackTrace();
                    continue;
                }
            }
        }

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
