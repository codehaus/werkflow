package com.werken.werkflow.engine;

import com.werken.werkflow.activity.Activity;

public class WorkflowActivity
    implements Activity
{
    private ActivityManager activityManager;
    private String caseId;
    private String transitionId;
    private String[] placeIds;

    public WorkflowActivity(ActivityManager activityManager,
                            String caseId,
                            String transitionId,
                            String[] placeIds)
    {
        this.activityManager = activityManager;
        this.caseId          = caseId;
        this.transitionId    = transitionId;
        this.placeIds        = placeIds;
    }

    public ActivityManager getActivityManager()
    {
        return this.activityManager;
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

    public void complete()
    {
        getActivityManager().complete( this );
    }

    public void completeWithError(Throwable error)
    {
        getActivityManager().completeWithError( this,
                                                error );
    }
}
