package com.werken.werkflow.event;

import com.werken.werkflow.Wfms;

public class ProcessUndeployedEvent
    extends WfmsEvent
{
    private String processId;

    public ProcessUndeployedEvent(Wfms wfms,
                                  String processId)
    {
        super( wfms );

        this.processId = processId;
    }

    public String getProcessId()
    {
        return this.processId;
    }
}
