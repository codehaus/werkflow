package com.werken.werkflow.engine;

import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.MessageSink;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.NoSuchMessageException;
import com.werken.werkflow.service.messaging.IncompatibleMessageSelectorException;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class MessageTypeCorrelator
    implements MessageSink
{
    private WorkflowEngine engine;
    private MessageType messageType;
    private Map msgWaiterCorrelators;
    private Registration registration;

    public MessageTypeCorrelator(WorkflowEngine engine,
                                 MessageType messageType)
        throws IncompatibleMessageSelectorException
    {
        this.engine               = engine;
        this.messageType          = messageType;
        this.msgWaiterCorrelators = new HashMap();

        this.registration = getEngine().register( this,
                                                  messageType );
    }

    public WorkflowEngine getEngine()
    {
        return this.engine;
    }

    public MessageType getMessageType()
    {
        return this.messageType;
    }

    Registration getRegistration()
    {
        return this.registration;
    }

    MessageWaiterCorrelator getMessageWaiterCorrelator(String transitionId)
    {
        return (MessageWaiterCorrelator) this.msgWaiterCorrelators.get( transitionId );
    }

    Message getMessage(String msgId)
        throws NoSuchMessageException
    {
        return getRegistration().getMessage( msgId );
    }
    
    void addMessageWaiter(String transitionId,
                          MessageWaiter messageWaiter)
    {
        MessageWaiterCorrelator msgWaiterCorrelator = new MessageWaiterCorrelator( getEngine(),
                                                                                   this,
                                                                                   transitionId,
                                                                                   messageWaiter );

        this.msgWaiterCorrelators.put( transitionId,
                                       msgWaiterCorrelator );
    }

    public void acceptMessage(Message message)
    {
        Iterator correlatorIter = this.msgWaiterCorrelators.values().iterator();

        MessageWaiterCorrelator eachCorrelator = null;

        while ( correlatorIter.hasNext() )
        {
            eachCorrelator = (MessageWaiterCorrelator) correlatorIter.next();

            eachCorrelator.acceptMessage( message );
        }
    }

    void evaluateCase(WorkflowProcessCase processCase,
                      String[] transitionIds)
    {
        Iterator correlatorIter = this.msgWaiterCorrelators.values().iterator();

        MessageWaiterCorrelator eachCorrelator = null;

        while ( correlatorIter.hasNext() )
        {
            eachCorrelator = (MessageWaiterCorrelator) correlatorIter.next();

            eachCorrelator.evaluateCase( processCase,
                                         transitionIds );
        }
    }

    boolean isCorrelated(String processCaseId,
                         String transitionId)
    {
        MessageWaiterCorrelator msgWaiterCorrelator = getMessageWaiterCorrelator( transitionId );

        return msgWaiterCorrelator.isCorrelated( processCaseId );
    }

    Object consumeMessage(String processCaseId,
                          String transitionId)
        throws NoSuchCorrelationException
    {
        MessageWaiterCorrelator msgWaiterCorrelator = getMessageWaiterCorrelator( transitionId );

        return msgWaiterCorrelator.consumeMessage( processCaseId );
    }
}
