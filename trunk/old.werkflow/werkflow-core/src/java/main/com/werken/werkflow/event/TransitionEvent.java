package com.werken.werkflow.event;

import com.werken.werkflow.Wfms;

public class TransitionEvent
    extends WfmsEvent
{
    private String processId;
    private String caseId;
    private String transitionId;

    public TransitionEvent(Wfms wfms,
                           String processId,
                           String caseId,
                           String transitionId)
    {
        super( wfms );

        this.processId     = processId;
        this.caseId        = caseId;
        this.transitionId  = transitionId;
    }

    public String getProcessId()
    {
        return this.processId;
    }

    public String getCaseId()
    {
        return this.caseId;
    }

    public String getTransitionId()
    {
        return this.transitionId;
    }
}
