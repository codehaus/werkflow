package com.werken.werkflow.service.caserepo;

import com.werken.werkflow.Attributes;

public interface CaseRepository
{
    public static final String ROLE = CaseRepository.class.getName();

    CaseState newCaseState(String processId,
                           Attributes attributes);

    CaseState getCaseState(String caseId);
}
