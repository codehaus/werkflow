package com.werken.werkflow.service.caserepo;

import java.util.Map;
import java.util.HashMap;

public class InMemoryCaseRepository
    extends AbstractCaseRepository
{
    private long counter;
    private Map cases;
    
    public InMemoryCaseRepository()
    {
        this.counter = 0;
        this.cases   = new HashMap();
    }

    protected synchronized String nextCaseId()
    {
        return "case." + (++counter);
    }

    protected void store(CaseState state)
    {
        this.cases.put( state.getCaseId(),
                        state );
    }

    protected CaseState retrieve(String caseId)
    {
        return (CaseState) this.cases.get( caseId );
    }

    
}
