package com.werken.werkflow.service.persistence;

public interface CorrelationTransfer
{
    static final CorrelationTransfer[] EMPTY_ARRAY = new CorrelationTransfer[0];

    static final short ADD_CORRELATION = 1;
    static final short REMOVE_CORRELATION = 2;

    short getType();

    String getCaseId();
    String getTransitionId();
    String getMessageId();
}
