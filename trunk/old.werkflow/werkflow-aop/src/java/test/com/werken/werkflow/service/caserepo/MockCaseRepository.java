package com.werken.werkflow.service.caserepo;

public class MockCaseRepository
    extends AbstractCaseRepository
{
    private String id;
    private CaseState stored;
    private CaseState retrieved;

    public MockCaseRepository()
    {
        setNextCaseId( "next.case.id" );
    }

    public void setNextCaseId(String id)
    {
        this.id = id;
    }

    public String nextCaseId()
    {
        return this.id;
    }

    public void store(CaseState state)
    {
        this.stored = state;
    }

    public CaseState getStored()
    {
        return this.stored;
    }

    public CaseState retrieve(String caseId)
    {
        return this.retrieved;
    }

    public void setRetrieved(CaseState retrieved)
    {
        this.retrieved = retrieved;
    }

    public String[] selectCases(String processId,
                                String placeId)
    {
        if ( getStored().getProcessId().equals( processId )
             &&
             getStored().hasMark( placeId ) )
        {
            return new String[] { getStored().getCaseId() };
        }

        return new String[0];
    }
}
