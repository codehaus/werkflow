/*
 * Created on Apr 6, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.werken.werkflow.service.persistence.prevayler;

import com.werken.werkflow.Attributes;
import com.werken.werkflow.service.persistence.CaseTransfer;
import com.werken.werkflow.service.persistence.ChangeSet;
import com.werken.werkflow.service.persistence.CorrelationTransfer;
import com.werken.werkflow.service.persistence.PersistenceException;

/**
 * @author kevin
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
class ActiveDelegate implements MethodDelegate
{
    private ProcessState _state;

    public ActiveDelegate(ProcessState state)
    {
        _state = state;
    }

    /**
     * @see com.werken.werkflow.service.persistence.prevayler.MethodDelegate#persist(null, com.werken.werkflow.service.persistence.ChangeSet)
     */
    public void persist(ChangeSet changeSet) throws PersistenceException
    {
        // @todo - Implement method
        throw new RuntimeException("Method not implemented");
    }

    /**
     * @see com.werken.werkflow.service.persistence.prevayler.MethodDelegate#hasCase(null, java.lang.String)
     */
    public boolean hasCase(String caseId)
    {
        return _state.hasCase(caseId);
    }

    /**
     * @see com.werken.werkflow.service.persistence.prevayler.MethodDelegate#newCase(null, com.werken.werkflow.Attributes)
     */
    public CaseTransfer newCase(Attributes initialiAttrs) throws PersistenceException
    {
        // @todo - Implement method
        throw new RuntimeException("Method not implemented");
    }

    /**
     * @see com.werken.werkflow.service.persistence.prevayler.MethodDelegate#loadCase(null, java.lang.String)
     */
    public CaseTransfer loadCase(String caseId) throws PersistenceException
    {
        return _state.loadCase(caseId);
    }

    /**
     * @see com.werken.werkflow.service.persistence.prevayler.MethodDelegate#getCorrelations(null)
     */
    public CorrelationTransfer[] getCorrelations() throws PersistenceException
    {
        // @todo - Implement method
        throw new RuntimeException("Method not implemented");
    }
}
