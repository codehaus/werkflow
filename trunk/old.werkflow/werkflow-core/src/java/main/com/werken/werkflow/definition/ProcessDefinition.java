package com.werken.werkflow.definition;

import com.werken.werkflow.definition.petri.Net;

import com.werken.werkflow.ProcessInfo;

public class ProcessDefinition
    implements ProcessInfo
{
    public static final ProcessDefinition[] EMPTY_ARRAY = new ProcessDefinition[0];

    private String id;
    private String documentation;
    private Net net;
    private MessageType[] messageTypes;

    public ProcessDefinition(String id,
                             Net net,
                             MessageType[] messageTypes)
    {
        this.id  = id;
        this.net = net;
        this.messageTypes = messageTypes;
    }

    public String getId()
    {
        return this.id;
    }

    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    public String getDocumentation()
    {
        return this.documentation;
    }
    
    public Net getNet()
    {
        return this.net;
    }

    public MessageType[] getMessageTypes()
    {
        return this.messageTypes;
    }
}
