package com.werken.werkflow.service.persistence;

import com.werken.werkflow.Attributes;

public interface ProcessPersistenceManager
{
    void persist(ChangeSet changeSet)
        throws PersistenceException;

    boolean hasCase(String caseId)
        throws PersistenceException;

    CaseTransfer newCase(Attributes initialiAttrs)
        throws PersistenceException;

    CaseTransfer loadCase(String caseId)
        throws PersistenceException;

    CorrelationTransfer[] getCorrelations()
        throws PersistenceException;
}
