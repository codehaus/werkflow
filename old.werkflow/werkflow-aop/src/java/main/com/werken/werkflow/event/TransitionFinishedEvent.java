package com.werken.werkflow.event;

import com.werken.werkflow.Wfms;

public class TransitionFinishedEvent
    extends TransitionEvent
{
    private Exception error;

    public TransitionFinishedEvent(Wfms wfms,
                                   String processId,
                                   String caseId,
                                   String transitionId)
    {
        super( wfms,
               processId,
               caseId,
               transitionId );
    }

    public TransitionFinishedEvent(Wfms wfms,
                                   String processId,
                                   String caseId,
                                   String transitionId,
                                   Exception error)
    {
        super( wfms,
               processId,
               caseId,
               transitionId );

        this.error = error;
    }

    public Exception getError()
    {
        return this.error;
    }

    public boolean finishedWithError()
    {
        return ( this.error != null );
    }
}
