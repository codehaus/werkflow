package com.werken.werkflow.service.caserepo;

import com.werken.werkflow.Attributes;

public interface CaseRepository
{
    CaseState newCaseState(String processId,
                           Attributes attributes);

    CaseState getCaseState(String caseId);
}
