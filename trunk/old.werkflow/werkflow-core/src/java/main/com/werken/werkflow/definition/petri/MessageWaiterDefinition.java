package com.werken.werkflow.definition.petri;

public class MessageWaiterDefinition
    extends WaiterDefinition
{
    private String messageType;
    private String correlator;
    private String var;

    public MessageWaiterDefinition(String messageType)
    {
        this( messageType,
              null,
              null );
    }

    public MessageWaiterDefinition(String messageType,
                                   String correlator,
                                   String var)
    {
        this.messageType = messageType;
        this.correlator  = correlator;
        this.var         = var;
    }

    public String getMessageType()
    {
        return this.messageType;
    }

    public String getCorrelator()
    {
        return this.correlator;
    }

    public String getVar()
    {
        return this.var;
    }
}
