package com.werken.werkflow.event;

import com.werken.werkflow.Wfms;

public class TokensProducedEvent
    extends TokensEvent
{
    public TokensProducedEvent(Wfms wfms,
                               String processId,
                               String caseId,
                               String transitionId,
                               String[] placeIds)
    {
        super( wfms,
               processId,
               caseId,
               transitionId,
               placeIds );
    }
}
