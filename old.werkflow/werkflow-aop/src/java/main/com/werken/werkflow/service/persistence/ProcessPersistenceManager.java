package com.werken.werkflow.service.persistence;

public interface ProcessPersistenceManager
{
    void persist(Transaction transaction);

    CaseTransfer newCase(String caseId);

    CaseTransfer loadCase(String caseId);

    CorrelationTransfer[] getCorrelations();
}
