package com.werken.werkflow.service.persistence.fleeting;

import com.werken.werkflow.service.persistence.ChangeSet;
import com.werken.werkflow.service.persistence.CaseTransfer;
import com.werken.werkflow.service.persistence.CorrelationTransfer;
import com.werken.werkflow.service.persistence.ProcessPersistenceManager;
import com.werken.werkflow.service.persistence.PersistenceException;

public class FleetingProcessPersistenceManager
    implements ProcessPersistenceManager
{
    private String packageId;
    private String processId;

    public FleetingProcessPersistenceManager(String packageId,
                                             String processId)
    {
        this.packageId = packageId;
        this.processId = processId;
    }

    public void persist(ChangeSet changeSet)
        throws PersistenceException
    {

    }

    public CaseTransfer newCase(String caseId)
        throws PersistenceException
    {
        return null;
    }

    public CaseTransfer loadCase(String caseId)
        throws PersistenceException
    {
        return null;
    }

    public CorrelationTransfer[] getCorrelations()
        throws PersistenceException
    {
        return CorrelationTransfer.EMPTY_ARRAY;
    }
}
