package com.werken.werkflow.service.persistence;

import java.util.Map;

public interface CaseTransfer
{
    static final CaseTransfer[] EMPTY_ARRAY = new CaseTransfer[0];

    String getCaseId();

    Map getAttributes();

    String[] getTokens();
}
