package com.werken.werkflow.service.persistence;

public interface CorrelationTransfer
{
    static final CorrelationTransfer[] EMPTY_ARRAY = new CorrelationTransfer[0];

    String getCaseId();
    String getTransitionId();
    String getMessageId();
}
