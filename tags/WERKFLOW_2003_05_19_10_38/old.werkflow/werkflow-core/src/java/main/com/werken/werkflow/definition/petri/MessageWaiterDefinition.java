package com.werken.werkflow.definition.petri;

public class MessageWaiterDefinition
    extends WaiterDefinition
{
    private String messageType;
    private String correlator;
    private String bind;

    public MessageWaiterDefinition(String messageType)
    {
        this( messageType,
              null,
              null );
    }

    public MessageWaiterDefinition(String messageType,
                                   String correlator,
                                   String bind)
    {
        this.messageType = messageType;
        this.correlator  = correlator;
        this.bind         = bind;
    }

    public String getMessageType()
    {
        return this.messageType;
    }

    public String getCorrelator()
    {
        return this.correlator;
    }

    public String getBind()
    {
        return this.bind;
    }
}
