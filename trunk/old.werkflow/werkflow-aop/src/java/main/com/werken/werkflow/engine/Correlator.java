package com.werken.werkflow.engine;

import com.werken.werkflow.NoSuchCaseException;
import com.werken.werkflow.NoSuchProcessException;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.MessageCorrelator;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.IncompatibleMessageSelectorException;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class Correlator
{
    private WorkflowEngine engine;
    private ProcessDeployment deployment;

    private Map msgTypeCorrelators;

    public Correlator(WorkflowEngine engine,
                      ProcessDeployment deployment)
    {
        this.engine             = engine;
        this.deployment         = deployment;
        this.msgTypeCorrelators = new HashMap();
    }

    public WorkflowEngine getEngine()
    {
        return this.engine;
    }

    public ProcessDeployment getProcessDeployment()
    {
        return this.deployment;
    }

    void addMessageWaiter(String transitionId,
                          MessageWaiter messageWaiter)
        throws IncompatibleMessageSelectorException
    {
        MessageType messageType = messageWaiter.getMessageType();

        MessageTypeCorrelator msgTypeCorrelator = getOrCreateMessageTypeCorrelator( messageType );

        msgTypeCorrelator.addMessageWaiter( transitionId,
                                            messageWaiter );
    }

    MessageTypeCorrelator getOrCreateMessageTypeCorrelator(MessageType messageType)
        throws IncompatibleMessageSelectorException
    {
        MessageTypeCorrelator correlator = getMessageTypeCorrelator( messageType );

        if ( correlator == null )
        {
            correlator = new MessageTypeCorrelator( getEngine(),
                                                    messageType );

            this.msgTypeCorrelators.put( messageType.getId(),
                                         correlator );
        }

        return correlator;
    }

    MessageTypeCorrelator getMessageTypeCorrelator(MessageType messageType)
    {
        return (MessageTypeCorrelator) this.msgTypeCorrelators.get( messageType.getId() );
    }

    void evaluateCase(WorkflowProcessCase processCase,
                      String[] transitionIds)
    {
        Iterator msgTypeCorrelatorIter = this.msgTypeCorrelators.values().iterator();

        MessageTypeCorrelator eachMsgTypeCorrelator = null;

        while ( msgTypeCorrelatorIter.hasNext() )
        {
            eachMsgTypeCorrelator = (MessageTypeCorrelator) msgTypeCorrelatorIter.next();

            eachMsgTypeCorrelator.evaluateCase( processCase,
                                                transitionIds );
        }
    }

    boolean isCorrelated(String processCaseId,
                         Transition transition)
    {
        MessageWaiter msgWaiter = transition.getMessageWaiter();

        MessageTypeCorrelator msgTypeCorrelator = getMessageTypeCorrelator( msgWaiter.getMessageType() );

        return msgTypeCorrelator.isCorrelated( processCaseId,
                                               transition.getId() );
    }

    Object consumeMessage(String processCaseId,
                          Transition transition)
        throws NoSuchCorrelationException
    {
        MessageWaiter msgWaiter = transition.getMessageWaiter();

        MessageTypeCorrelator msgTypeCorrelator = getMessageTypeCorrelator( msgWaiter.getMessageType() );

        return msgTypeCorrelator.consumeMessage( processCaseId,
                                                 transition.getId() );
    }
}

