package com.werken.werkflow.core;

class ActivityKey
{
    private String packageId;
    private String processId;
    private String caseId;
    private String transitionId;

    ActivityKey(String packageId,
                String processId,
                String caseId,
                String transitionId)
    {
        this.packageId    = packageId;
        this.processId    = processId;
        this.caseId       = caseId;
        this.transitionId = transitionId;
    }

    String getPackageId()
    {
        return this.packageId;
    }

    String getProcessId()
    {
        return this.processId;
    }

    String getCaseId()
    {
        return this.caseId;
    }

    String getTransitionId()
    {
        return this.transitionId;
    }

    public boolean equals(Object thatObj)
    {
        if ( this == thatObj )
        {
            return true;
        }

        if ( thatObj instanceof ActivityKey )
        {
            ActivityKey that = (ActivityKey) thatObj;

            return ( getPackageId().equals( that.getPackageId() )
                     &&
                     getProcessId().equals( that.getProcessId() )
                     &&
                     getCaseId().equals( that.getCaseId() )
                     &&
                     getTransitionId().equals( that.getTransitionId() ) );
        }

        return false;
    }

    public int hashCode()
    {
        return ( getPackageId().hashCode() / 4
                 +
                 getProcessId().hashCode() / 4
                 +
                 getCaseId().hashCode() / 4
                 +
                 getTransitionId().hashCode() / 4 );
    }
}
