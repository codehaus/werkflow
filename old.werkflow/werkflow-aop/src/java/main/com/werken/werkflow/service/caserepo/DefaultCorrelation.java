package com.werken.werkflow.service.caserepo;

public class DefaultCorrelation
    implements Correlation
{
    private String caseId;
    private String transitionId;
    private String messageId;

    public DefaultCorrelation(String caseId,
                              String transitionId,
                              String messageId)
    {
        this.caseId       = caseId;
        this.transitionId = transitionId;
        this.messageId    = messageId;
    }

    public String getCaseId()
    {
        return this.caseId;
    }

    public String getTransitionId()
    {
        return this.transitionId;
    }

    public String getMessageId()
    {
        return this.messageId;
    }

    public boolean equals(Object thatObj)
    {
        if ( this == thatObj )
        {
            return true;
        }

        if ( thatObj instanceof Correlation )
        {
            Correlation that = (Correlation) thatObj;

            return ( this.getCaseId().equals( that.getCaseId() )
                     &&
                     this.getTransitionId().equals( that.getTransitionId() )
                     &&
                     this.getMessageId().equals( that.getMessageId() ) );
        }

        return false;
    }

    public int hashCode()
    {
        return this.caseId.hashCode()
            + this.transitionId.hashCode()
            + this.messageId.hashCode();
    }
}
