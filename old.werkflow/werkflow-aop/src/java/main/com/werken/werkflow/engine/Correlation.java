package com.werken.werkflow.engine;

public class Correlation
{
    private String messageId;
    private String processCaseId;

    public Correlation(String messageId,
                       String processCaseId)
    {
        this.messageId     = messageId;
        this.processCaseId = processCaseId;
    }

    public String getMessageId()
    {
        return this.messageId;
    }

    public String getProcessCaseId()
    {
        return this.processCaseId;
    }

    public int hashCode()
    {
        return getMessageId().hashCode() + getProcessCaseId().hashCode();
    }

    public boolean equals(Object thatObj)
    {
        if ( thatObj instanceof Correlation )
        {
            Correlation that = (Correlation) thatObj;

            return ( getMessageId().equals( that.getMessageId() )
                     &&
                     getProcessCaseId().equals( that.getProcessCaseId() ) );
        }

        return false;
    }
}
