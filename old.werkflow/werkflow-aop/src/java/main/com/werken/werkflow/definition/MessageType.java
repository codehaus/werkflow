package com.werken.werkflow.definition;

import com.werken.werkflow.service.messaging.MessageSelector;

public class MessageType
{
    private String id;
    private MessageSelector selector;

    public MessageType(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public void setMessageSelector(MessageSelector selector)
    {
        this.selector = selector;
    }

    public MessageSelector getMessageSelector()
    {
        return this.selector;
    }
}
