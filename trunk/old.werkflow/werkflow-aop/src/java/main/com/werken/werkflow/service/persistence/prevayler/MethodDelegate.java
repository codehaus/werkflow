/*
 * Created on Apr 6, 2003
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.werken.werkflow.service.persistence.prevayler;

import java.io.Serializable;

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
interface MethodDelegate extends Serializable
{
    /**
     * @see com.werken.werkflow.service.persistence.ProcessPersistenceManager#persist(com.werken.werkflow.service.persistence.ChangeSet)
     */
    void persist(ChangeSet changeSet) throws PersistenceException;
    /**
     * @see com.werken.werkflow.service.persistence.ProcessPersistenceManager#hasCase(java.lang.String)
     */
    boolean hasCase(String caseId);
    /**
     * @see com.werken.werkflow.service.persistence.ProcessPersistenceManager#newCase(com.werken.werkflow.Attributes)
     */
    CaseTransfer newCase(Attributes initialiAttrs) throws PersistenceException;
    /**
     * @see com.werken.werkflow.service.persistence.ProcessPersistenceManager#loadCase(java.lang.String)
     */
    CaseTransfer loadCase(String caseId) throws PersistenceException;
    /**
     * @see com.werken.werkflow.service.persistence.ProcessPersistenceManager#getCorrelations()
     */
    CorrelationTransfer[] getCorrelations() throws PersistenceException;
}
