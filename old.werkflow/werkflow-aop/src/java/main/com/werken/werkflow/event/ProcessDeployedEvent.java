package com.werken.werkflow.event;

import com.werken.werkflow.Wfms;
import com.werken.werkflow.definition.ProcessDefinition;

public class ProcessDeployedEvent
    extends WfmsEvent
{
    private ProcessDefinition processDef;

    public ProcessDeployedEvent(Wfms wfms,
                                ProcessDefinition processDef)
    {
        super( wfms );

        this.processDef = processDef;
    }

    public ProcessDefinition getProcessDefinition()
    {
        return this.processDef;
    }
}
