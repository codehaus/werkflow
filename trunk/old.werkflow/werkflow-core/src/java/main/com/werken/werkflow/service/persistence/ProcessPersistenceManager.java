package com.werken.werkflow.service.persistence;

public interface ProcessPersistenceManager
{
    void persist(ChangeSet changeSet)
        throws PersistenceException;

    CaseTransfer newCase(String caseId)
        throws PersistenceException;

    CaseTransfer loadCase(String caseId)
        throws PersistenceException;

    CorrelationTransfer[] getCorrelations()
        throws PersistenceException;
}
