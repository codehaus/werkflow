package com.werken.werkflow.service.messaging;

public class IncompatibleMessageSelectorException
    extends MessagingException
{
    private MessageSelector selector;

    public IncompatibleMessageSelectorException(MessageSelector selector)
    {
        this.selector = selector;
    }

    public MessageSelector getMessageSelector()
    {
        return this.selector;
    }
}
