package org.codehaus.werkflow.service.persistence.prevayler;

import org.codehaus.werkflow.service.persistence.CaseTransfer;
import org.codehaus.werkflow.service.persistence.ChangeSet;
import org.codehaus.werkflow.service.persistence.PersistenceException;
import org.prevayler.Transaction;
import org.python.core.StdoutWrapper;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 8/09/2003
 * Time: 13:38:48
 * To change this template use Options | File Templates.
 */
public class PersistChangeSet implements Transaction
{
    public PersistChangeSet( ChangeSet changeSet )
    {
        _changeSet = new SerializableChangeSet(changeSet);
    }

    private ChangeSet _changeSet;

    public void executeOn( Object object )
    {
        if ( !( object instanceof ProcessStore ) )
        {
            throw new IllegalArgumentException( "This command operates on a ProcessStore PrevalentSystem." );
        }

        ProcessStore store = (ProcessStore) object;

        CaseTransfer[] transfers = _changeSet.getModifiedCases();
        for ( int i = 0; i < transfers.length; i++ )
        {
            CaseTransfer transfer = transfers[i];
            transfer.getCaseId();

            try
            {
                CaseState state = store.fetchCase( transfer.getPackageId(), transfer.getProcessId(), transfer.getCaseId() );
                state.setTokens(transfer.getTokens());
                state.setAttributes(transfer.getAttributes());
            }
            catch ( PersistenceException e )
            {
                e.printStackTrace();  //To change body of catch statement use Options | File Templates.
            }
        }

    }

    private static class SerializableChangeSet implements ChangeSet
    {

        public SerializableChangeSet( ChangeSet changeset )
        {
            _transfers = changeset.getModifiedCases();
        }

        CaseTransfer[] _transfers;

        public CaseTransfer[] getModifiedCases()
        {
            return _transfers;
        }

    }
}
