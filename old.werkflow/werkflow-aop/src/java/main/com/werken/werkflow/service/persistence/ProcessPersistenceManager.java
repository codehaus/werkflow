package com.werken.werkflow.service.persistence;

public interface ProcessPersistenceManager
{
    void persist(ChangeSet changeSet);

    CaseTransfer newCase(String caseId);

    CaseTransfer loadCase(String caseId);

    CorrelationTransfer[] getCorrelations();
}
