package com.werken.werkflow.core;

import com.werken.werkflow.service.persistence.PersistenceException;

interface ChangeSetSource
{
    CoreChangeSet newChangeSet();

    void commit(CoreChangeSet changeSet)
        throws PersistenceException;
}
