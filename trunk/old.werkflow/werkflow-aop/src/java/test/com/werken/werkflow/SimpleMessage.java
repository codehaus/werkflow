package com.werken.werkflow;

public class SimpleMessage
{
    private String type;
    private String payload;

    public SimpleMessage(String type,
                         String payload)
    {
        this.type    = type;
        this.payload = payload;
    }

    public String getType()
    {
        return this.type;
    }

    public String getPayload()
    {
        return this.payload;
    }
}
