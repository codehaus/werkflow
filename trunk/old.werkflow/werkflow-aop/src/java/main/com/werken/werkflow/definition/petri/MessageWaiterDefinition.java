package com.werken.werkflow.definition.petri;

public class MessageWaiterDefinition
    extends WaiterDefinition
{
    private String messageType;
    private String correlator;

    public MessageWaiterDefinition(String messageType)
    {
        this( messageType,
              null );
    }

    public MessageWaiterDefinition(String messageType,
                                   String correlator)
    {
        this.messageType = messageType;
        this.correlator  = correlator;
    }

    public String getMessageType()
    {
        return this.messageType;
    }

    public String getCorrelator()
    {
        return this.correlator;
    }
}
