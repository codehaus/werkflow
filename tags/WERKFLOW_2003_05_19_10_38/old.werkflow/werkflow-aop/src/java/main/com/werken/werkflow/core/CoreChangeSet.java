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
    private List correlations;
    private List consumedMessages;

    CoreChangeSet(ChangeSetSource changeSourceSet)
    {
        this.changeSetSource  = changeSourceSet;
        this.correlations     = new ArrayList();
        this.modifiedCases    = new HashSet();
        this.consumedMessages = new ArrayList();
    }

    void addModifiedCase(CoreProcessCase processCase)
    {
        this.modifiedCases.add( processCase );
    }

    CoreProcessCase[] getCoreModifiedCases()
    {
        return (CoreProcessCase[]) this.modifiedCases.toArray( CoreProcessCase.EMPTY_ARRAY );
    }

    void addConsumption(String messageId)
    {
        this.consumedMessages.add( messageId );
    }

    String[] getCoreConsumptions()
    {
        return (String[]) this.consumedMessages.toArray( EMPTY_STRING_ARRAY );
    }

    void addCorrelation(Correlation correlation)
    {
        this.correlations.add( correlation );
    }

    Correlation[] getCoreCorrelations()
    {
        return (Correlation[]) this.correlations.toArray( Correlation.EMPTY_ARRAY );
    }

    void addUncorrelated(Message message)
    {

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

    public CorrelationTransfer[] getCorrelations()
    {
        return null;
    }

    public UncorrelatedTransfer[] getUncorrelateds()
    {
        return null;
    }

    public ConsumptionTransfer[] getConsumptions()
    {
        return null;
    }
    
}
