package com.werken.werkflow.engine;

import com.werken.werkflow.activity.Activity;

import java.util.Map;

public class WorkflowActivity
    implements Activity
{
    private ActivityManager activityManager;
    private String caseId;
    private String transitionId;
    private String[] placeIds;
    private Map caseAttrs;

    public WorkflowActivity(ActivityManager activityManager,
                            String caseId,
                            String transitionId,
                            String[] placeIds,
                            Map caseAttrs)
    {
        this.activityManager = activityManager;
        this.caseId          = caseId;
        this.transitionId    = transitionId;
        this.placeIds        = placeIds;
        this.caseAttrs       = caseAttrs;
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

    public Map getCaseAttributes()
    {
        return this.caseAttrs;
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
