package com.werken.werkflow.admin;

import com.werken.werkflow.definition.ProcessDefinition;

public class ProcessException
    extends AdminException
{
    private ProcessDefinition processDef;
    
    public ProcessException(ProcessDefinition processDef)
    {
        this.processDef = processDef;
    }

    public ProcessDefinition getProcess()
    {
        return this.processDef;
    }

}
