package com.werken.werkflow.event;

import com.werken.werkflow.Wfms;

public class TokensEvent
    extends CaseEvent
{
    private String transitionId;
    private String[] placeIds;

    public TokensEvent(Wfms wfms,
                       String processId,
                       String caseId,
                       String transitionId,
                       String[] placeIds)
    {
        super( wfms,
               processId,
               caseId );

        this.placeIds  = placeIds;
    }

    public String getTransitionId()
    {
        return this.transitionId;
    }

    public String[] getPlaceIds()
    {
        return this.placeIds;
    }
}
