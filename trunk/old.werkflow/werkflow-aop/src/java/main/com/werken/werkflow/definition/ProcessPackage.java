package com.werken.werkflow.definition;

import java.util.List;
import java.util.ArrayList;

public class ProcessPackage
{
    private String id;
    private String documentation;
    private List processDefs;
    
    public ProcessPackage(String id)
    {
        this.id = id;
        this.processDefs = new ArrayList();
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

    public void addProcessDefinition(ProcessDefinition processDef)
    {
        this.processDefs.add( processDef );
    }

    public ProcessDefinition[] getProcessDefinitions()
    {
        return (ProcessDefinition[]) this.processDefs.toArray( ProcessDefinition.EMPTY_ARRAY );
    }
}
