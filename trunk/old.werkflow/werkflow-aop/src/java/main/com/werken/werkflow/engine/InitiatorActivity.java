package com.werken.werkflow.engine;

import com.werken.werkflow.activity.Activity;

public class InitiatorActivity
    implements Activity
{
    private Throwable error;

    public InitiatorActivity()
    {

    }

    public String getCaseId()
    {
        return null;
    }

    public String getTransitionId()
    {
        return null;
    }

    public void complete()
    {
    }

    public void completeWithError(Throwable error)
    {
        this.error = error;
    }

    public Throwable getError()
    {
        return this.error;
    }
}
