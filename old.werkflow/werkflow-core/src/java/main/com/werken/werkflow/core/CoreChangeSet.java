package com.werken.werkflow.core;

import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.persistence.ChangeSet;
import com.werken.werkflow.service.persistence.CaseTransfer;
import com.werken.werkflow.service.persistence.CorrelationTransfer;
import com.werken.werkflow.service.persistence.UncorrelatedTransfer;
import com.werken.werkflow.service.persistence.ConsumptionTransfer;
import com.werken.werkflow.service.persistence.PersistenceException;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

class CoreChangeSet
    implements ChangeSet
{
    private static String[] EMPTY_STRING_ARRAY = new String[0];

    private ChangeSetSource changeSetSource;

    private Set modifiedCases;

    CoreChangeSet(ChangeSetSource changeSourceSet)
    {
        this.changeSetSource  = changeSourceSet;
        this.modifiedCases    = new HashSet();
    }

    void addModifiedCase(CoreProcessCase processCase)
    {
        this.modifiedCases.add( processCase );
    }

    CoreProcessCase[] getCoreModifiedCases()
    {
        return (CoreProcessCase[]) this.modifiedCases.toArray( CoreProcessCase.EMPTY_ARRAY );
    }

    ChangeSetSource getChangeSetSource()
    {
        return this.changeSetSource;
    }

    void commit()
        throws PersistenceException
    {
        getChangeSetSource().commit( this );
    }

    public CaseTransfer[] getModifiedCases()
    {
        return null;
    }
}
