package com.werken.werkflow.definition;

import com.werken.werkflow.WerkflowException;

public class DuplicateMessageTypeException
    extends WerkflowException
{
    private MessageType messageType;

    public DuplicateMessageTypeException(MessageType messageType)
    {
        this.messageType = messageType;
    }

    public MessageType getMessageType()
    {
        return this.messageType;
    }

    public String getMessage()
    {
        return "duplicate message-type: " + getMessageType().getId();
    }
}
