package com.werken.werkflow.engine;

import com.werken.werkflow.NoSuchCaseException;
import com.werken.werkflow.NoSuchProcessException;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.MessageCorrelator;
import com.werken.werkflow.definition.MessageType;
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
        String msgTypeId = messageWaiter.getMessageTypeId();

        MessageTypeCorrelator msgTypeCorrelator = getMessageTypeCorrelator( msgTypeId );

        msgTypeCorrelator.addMessageWaiter( transitionId,
                                            messageWaiter );
    }

    MessageTypeCorrelator getMessageTypeCorrelator(String msgTypeId)
        throws IncompatibleMessageSelectorException
    {
        MessageTypeCorrelator correlator = (MessageTypeCorrelator) this.msgTypeCorrelators.get( msgTypeId );

        if ( correlator == null )
        {
            MessageType messageType = getProcessDeployment().getMessageType( msgTypeId );

            correlator = new MessageTypeCorrelator( getEngine(),
                                                    messageType );

            this.msgTypeCorrelators.put( msgTypeId,
                                         correlator );
        }

        return correlator;
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
}

