package com.werken.werkflow.event;

import com.werken.werkflow.Wfms;

public class TokensRolledBackEvent
    extends TokensEvent
{
    public TokensRolledBackEvent(Wfms wfms,
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
