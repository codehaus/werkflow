/*
 * Created on Apr 5, 2003
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
class PassiveDelegate implements MethodDelegate
{
    /**
     * @see com.werken.werkflow.service.persistence.ProcessPersistenceManager#persist(com.werken.werkflow.service.persistence.ChangeSet)
     */
    public void persist(ChangeSet changeSet) throws PersistenceException
    {
        throw new PersistenceException("Operation not allowed on a passivated manager.");
    }

    /**
     * @see com.werken.werkflow.service.persistence.ProcessPersistenceManager#hasCase(java.lang.String)
     */
    public boolean hasCase(String caseId)
    {
        // -- @todo should this be an exception ?
        return false; 
    }

    /**
     * @see com.werken.werkflow.service.persistence.ProcessPersistenceManager#newCase(com.werken.werkflow.Attributes)
     */
    public CaseTransfer newCase(Attributes initialiAttrs) throws PersistenceException
    {
        throw new PersistenceException("Operation not allowed on a passivated manager.");
    }

    /**
     * @see com.werken.werkflow.service.persistence.ProcessPersistenceManager#loadCase(java.lang.String)
     */
    public CaseTransfer loadCase(String caseId) throws PersistenceException
    {
        throw new PersistenceException("Operation not allowed on a passivated manager.");
    }

    /**
     * @see com.werken.werkflow.service.persistence.ProcessPersistenceManager#getCorrelations()
     */
    public CorrelationTransfer[] getCorrelations() throws PersistenceException
    {
        throw new PersistenceException("Operation not allowed on a passivated manager.");
    }

}
