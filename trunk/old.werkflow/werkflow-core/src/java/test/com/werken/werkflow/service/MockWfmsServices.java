package com.werken.werkflow.service;

import com.werken.werkflow.service.caserepo.CaseRepository;

public class MockWfmsServices
    implements WfmsServices
{
    private CaseRepository caseRepo;

    public MockWfmsServices()
    {
    }

    public void setCaseRepository(CaseRepository caseRepo)
    {
        this.caseRepo = caseRepo;
    }

    public CaseRepository getCaseRepository()
    {
        return this.caseRepo;
    }
}
