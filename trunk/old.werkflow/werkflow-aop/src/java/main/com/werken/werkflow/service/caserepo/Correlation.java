package com.werken.werkflow.service.caserepo;

public interface Correlation
{
    static final Correlation[] EMPTY_ARRAY = new Correlation[0];

    String getCaseId();
    String getTransitionId();
    String getMessageId();
}
