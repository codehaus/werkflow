package com.werken.werkflow.event;

import com.werken.werkflow.Wfms;

public class TransitionEvent
    extends CaseEvent
{
    private String transitionId;

    public TransitionEvent(Wfms wfms,
                           String processId,
                           String caseId,
                           String transitionId)
    {
        super( wfms,
               processId,
               caseId );

        this.transitionId  = transitionId;
    }

    public String getTransitionId()
    {
        return this.transitionId;
    }
}
