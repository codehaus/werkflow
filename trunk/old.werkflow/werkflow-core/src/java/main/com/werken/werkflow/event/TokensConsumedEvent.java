package com.werken.werkflow.event;

import com.werken.werkflow.Wfms;

public class TokensConsumedEvent
    extends TokensEvent
{
    public TokensConsumedEvent(Wfms wfms,
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
