package com.werken.werkflow.core;

import java.util.List;
import java.util.ArrayList;

class MockChangeSetSource
    implements ChangeSetSource
{
    private List committed;

    MockChangeSetSource()
    {
        this.committed = new ArrayList();
    }

    public CoreChangeSet newChangeSet()
    {
        return new CoreChangeSet( this );
    }

    public void commit(CoreChangeSet tx)
    {
        this.committed.add( tx );
    }

    List getCommittedTransactions()
    {
        return this.committed;
    }
}
