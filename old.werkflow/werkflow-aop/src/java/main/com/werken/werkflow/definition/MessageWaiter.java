package com.werken.werkflow.definition;

public class MessageWaiter
{
    private String messageTypeId;
    private MessageCorrelator messageCorrelator;

    public MessageWaiter(String messageTypeId)
    {
        this.messageTypeId = messageTypeId;
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
