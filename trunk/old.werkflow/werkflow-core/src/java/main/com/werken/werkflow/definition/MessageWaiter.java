package com.werken.werkflow.definition;

public class MessageWaiter
{
    private String messageTypeId;
    private String bindingVar;
    private MessageCorrelator messageCorrelator;

    public MessageWaiter(String messageTypeId,
                         String bindingVar)
    {
        this.messageTypeId = messageTypeId;
        this.bindingVar    = bindingVar;
    }

    public String getMessageTypeId()
    {
        return this.messageTypeId;
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
