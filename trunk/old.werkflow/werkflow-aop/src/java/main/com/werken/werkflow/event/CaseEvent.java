package com.werken.werkflow.event;

import com.werken.werkflow.Wfms;

public class CaseEvent
    extends WfmsEvent
{
    private String processId;
    private String caseId;

    public CaseEvent(Wfms wfms,
                     String processId,
                     String caseId)
    {
        super( wfms );

        this.processId = processId;
        this.caseId    = caseId;
    }

    public String getProcessId()
    {
        return this.processId;
    }

    public String getCaseId()
    {
        return this.caseId;
    }
}
