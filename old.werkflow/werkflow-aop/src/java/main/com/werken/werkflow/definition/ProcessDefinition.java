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

    public ProcessDefinition(String id,
                             Net net)
    {
        this.id  = id;
        this.net = net;
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
}

