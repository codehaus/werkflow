package com.werken.werkflow.engine.rules;

import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.engine.MessageBlocker;

import org.drools.spi.ObjectType;

public class MessageBlockerObjectType
    implements ObjectType
{
    private MessageType messageType;

    public MessageBlockerObjectType(MessageType messageType)
    {
        this.messageType = messageType;
    }

    public MessageType getMessageType()
    {
        return this.messageType;
    }

    public boolean matches(Object object)
    {
        if ( object instanceof MessageBlocker )
        {
            MessageBlocker blocker = (MessageBlocker) object;

            return blocker.getMessageType().equals( getMessageType() );
        }

        return false;
    }
}
