package com.werken.werkflow.event;

import com.werken.werkflow.Wfms;

public class TransitionInitiatedEvent
    extends TransitionEvent
{
    public TransitionInitiatedEvent(Wfms wfms,
                                    String processId,
                                    String caseId,
                                    String transitionId)
    {
        super( wfms,
               processId,
               caseId,
               transitionId );
    }
}
