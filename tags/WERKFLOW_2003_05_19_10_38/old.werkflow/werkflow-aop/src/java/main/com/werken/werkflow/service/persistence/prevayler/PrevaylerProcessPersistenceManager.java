package com.werken.werkflow.service.persistence.prevayler;

import java.io.Serializable;

import org.prevayler.Prevayler;

import com.werken.werkflow.Attributes;
import com.werken.werkflow.service.persistence.CaseTransfer;
import com.werken.werkflow.service.persistence.ChangeSet;
import com.werken.werkflow.service.persistence.CorrelationTransfer;
import com.werken.werkflow.service.persistence.PersistenceException;
import com.werken.werkflow.service.persistence.ProcessPersistenceManager;

class PrevaylerProcessPersistenceManager implements ProcessPersistenceManager
{
    public PrevaylerProcessPersistenceManager(Prevayler prevayler, ProcessState state)
    {
        if (null == state)
        {
            throw new IllegalArgumentException("The supplied ProcessState can not be null.");
        }
       
        _key = state.key();
        
        // new process managers are always active
        _delegate = new ActiveDelegate(prevayler, state); 
    }

    private MethodDelegate _delegate;
    private ManagerKey _key;

    // -- ProcessPersistenceManager implementation

    public void persist(ChangeSet changeSet) throws PersistenceException
    {
        _delegate.persist(changeSet);
    }

    public boolean hasCase(String caseId)
    {
        return _delegate.hasCase(caseId);
    }

    public CaseTransfer newCase(Attributes initialiAttrs) throws PersistenceException
    {
        return _delegate.newCase(initialiAttrs);
    }

    public CaseTransfer loadCase(String caseId) throws PersistenceException
    {
        return _delegate.loadCase(caseId);
    }

    public CorrelationTransfer[] getCorrelations() throws PersistenceException
    {
        return _delegate.getCorrelations();
    }

    // -- for my "friends"
    
    ManagerKey key()
    {
        return _key;        
    }
    
    void passivate()
    {
        _delegate = new PassiveDelegate();
    }
}
