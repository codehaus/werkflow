package com.werken.werkflow.event;

import com.werken.werkflow.Wfms;

public class TokensEvent
    extends WfmsEvent
{
    private String processId;
    private String caseId;
    private String transitionId;
    private String[] placeIds;

    public TokensEvent(Wfms wfms,
                       String processId,
                       String caseId,
                       String transitionId,
                       String[] placeIds)
    {
        super( wfms );

        this.processId = processId;
        this.caseId    = caseId;
        this.placeIds  = placeIds;
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

    public String[] getPlaceIds()
    {
        return this.placeIds;
    }
}
