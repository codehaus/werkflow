package com.werken.werkflow.engine.rules;

import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.engine.Message;

import org.drools.spi.ObjectType;

public class MessageTypeObjectType
    implements ObjectType
{
    private MessageType messageType;

    public MessageTypeObjectType(MessageType messageType)
    {
        this.messageType = messageType;
    }

    public MessageType getMessageType()
    {
        return this.messageType;
    }

    public boolean matches(Object object)
    {
        if ( object instanceof Message )
        {
            Message message = (Message) object;

            return message.getMessageType().equals( getMessageType() );
        }

        return false;
    }
}
