package com.werken.werkflow.event;

import com.werken.werkflow.Wfms;

public class TransitionStartedEvent
    extends TransitionEvent
{
    public TransitionStartedEvent(Wfms wfms,
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
