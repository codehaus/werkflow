package com.werken.werkflow.core;

import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.MessageSink;
import com.werken.werkflow.service.messaging.MessagingManager;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.NoSuchMessageException;
import com.werken.werkflow.service.messaging.IncompatibleMessageSelectorException;
import com.werken.werkflow.definition.Waiter;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.petri.Transition;

import java.util.Map;
import java.util.HashMap;

class MessageHandler
{
    private MessagingManager messagingManager;

    private MessageSink messageSink;

    private Map messageTypeHandlers;

    private Map messageTypeHandlersByTransitionId;

    MessageHandler(MessagingManager  messagingManager,
                   MessageSink messageSink)
    {
        this.messagingManager    = messagingManager;
        this.messageSink         = messageSink;

        this.messageTypeHandlers               = new HashMap();
        this.messageTypeHandlersByTransitionId = new HashMap();
    }

    MessagingManager getMessagingManager()
    {
        return this.messagingManager;
    }

    MessageSink getMessageSink()
    {
        return this.messageSink;
    }

    void add(Transition transition,
             ProcessDeployment deployment)
        throws IncompatibleMessageSelectorException
    {
        MessageWaiter waiter = (MessageWaiter) transition.getWaiter();

        MessageType messageType = waiter.getMessageType();

        MessageTypeHandler handler = (MessageTypeHandler) this.messageTypeHandlers.get( messageType );

        if ( handler == null )
        {
            Registration registration = getMessagingManager().register( getMessageSink(),
                                                                        messageType );

            handler = new MessageTypeHandler( registration );

            System.err.println( "new message type handle: " + handler + " // " + registration );

            this.messageTypeHandlers.put( messageType,
                                          handler );

            this.messageTypeHandlersByTransitionId.put( transition.getId(),
                                                        handler );
        }

        handler.add( transition,
                     deployment );
    }

    boolean acceptMessage(Message message)
    {
        System.err.println( "MessageHandler.acceptMessage( " + message.getMessage() + " )" );
        return getMessageTypeHandler( message.getMessageType() ).acceptMessage( message );
    }

    Message consumeMessage(CoreChangeSet changeSet,
                           CoreProcessCase processCase,
                           String transitionId,
                           String messageId)
        throws NoSuchMessageException
    {
        return getMessageTypeHandler( transitionId ).consumeMessage( changeSet,
                                                                     processCase,
                                                                     transitionId,
                                                                     messageId );
    }

    boolean addCase(CoreProcessCase processCase,
                    String transitionId)
    {
        return getMessageTypeHandler( transitionId ).addCase( processCase,
                                                              transitionId );
    }

    void removeCase(CoreProcessCase processCase,
                    String transitionId)
    {
        getMessageTypeHandler( transitionId ).removeCase( processCase,
                                                          transitionId );
    }

    private MessageTypeHandler getMessageTypeHandler(MessageType messageType)
    {
        return (MessageTypeHandler) this.messageTypeHandlers.get( messageType );
    }

    private MessageTypeHandler getMessageTypeHandler(String transitionId)
    {
        return (MessageTypeHandler) this.messageTypeHandlersByTransitionId.get( transitionId );
    }
}