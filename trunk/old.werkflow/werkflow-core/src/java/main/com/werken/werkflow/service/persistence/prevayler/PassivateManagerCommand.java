/*
 * Created on Apr 5, 2003
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.werken.werkflow.service.persistence.prevayler;

import org.prevayler.util.TransactionWithQuery;

import com.werken.werkflow.service.persistence.PersistenceException;

/**
 * @author kevin
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
class PassivateManagerCommand extends TransactionWithQuery
{
    public PassivateManagerCommand(String packageId, String processId)
    {
        _package = packageId;
        _process = processId;
    }

    private String _package;
    private String _process;

    protected Object executeAndQuery(Object prevalentSystem) throws PersistenceException
    {
        if (!(prevalentSystem instanceof ProcessStore))
        {
            throw new IllegalArgumentException("This command operates on a ProcessStore PrevalentSystem.");
        }

        ProcessStore store = (ProcessStore) prevalentSystem;
        store.passivateManager(_package, _process);

        return null;
    }
}
