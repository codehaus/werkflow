package com.werken.werkflow.activity;

public interface Activity
{
    static final Activity[] EMPTY_ARRAY = new Activity[0];

    String getCaseId();
    String getTransitionId();

    void complete();
    void completeWithError(Throwable error);
}
