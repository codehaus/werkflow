package com.werken.werkflow.definition;

public class MessageWaiter
{
    private MessageType messageType;
    private String bindingVar;
    private MessageCorrelator messageCorrelator;

    public MessageWaiter(MessageType messageType,
                         String bindingVar)
    {
        this.messageType = messageType;
        this.bindingVar    = bindingVar;
    }

    public MessageType getMessageType()
    {
        return this.messageType;
    }

    public String getBindingVar()
    {
        return this.bindingVar;
    }

    public void setMessageCorrelator(MessageCorrelator messageCorrelator)
    {
        this.messageCorrelator = messageCorrelator;
    }

    public MessageCorrelator getMessageCorrelator()
    {
        return this.messageCorrelator;
    }
}
