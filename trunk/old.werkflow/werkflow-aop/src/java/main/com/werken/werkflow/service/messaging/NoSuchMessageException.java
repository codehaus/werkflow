package com.werken.werkflow.service.messaging;

public class NoSuchMessageException
    extends MessagingException
{
    private String id;

    public NoSuchMessageException(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }
}
