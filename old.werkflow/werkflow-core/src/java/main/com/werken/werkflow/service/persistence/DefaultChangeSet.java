package com.werken.werkflow.service.persistence;

import java.util.List;
import java.util.ArrayList;

public class DefaultChangeSet
    implements ChangeSet
{
    private List modifiedCases;
    private List correlations;
    private List uncorrelateds;
    private List consumptions;

    public DefaultChangeSet()
    {
        this.modifiedCases = new ArrayList();
        this.correlations  = new ArrayList();
        this.uncorrelateds = new ArrayList();
        this.consumptions  = new ArrayList();
    }

    public void addModifiedCase(CaseTransfer modifiedCase)
    {
        this.modifiedCases.add( modifiedCase );
    }

    public void addCorrelation(CorrelationTransfer correlation)
    {
        this.correlations.add( correlation );
    }

    public void addUncorrelated(UncorrelatedTransfer uncorrelated)
    {
        this.uncorrelateds.add( uncorrelated );
    }

    public void addConsumption(ConsumptionTransfer consumption)
    {
        this.consumptions.add( consumption );
    }

    public CaseTransfer[] getModifiedCases()
    {
        return (CaseTransfer[]) this.modifiedCases.toArray( CaseTransfer.EMPTY_ARRAY );
    }

    public CorrelationTransfer[] getCorrelations()
    {
        return (CorrelationTransfer[]) this.correlations.toArray( CorrelationTransfer.EMPTY_ARRAY );
    }

    public UncorrelatedTransfer[] getUncorrelateds()
    {
        return (UncorrelatedTransfer[]) this.uncorrelateds.toArray( UncorrelatedTransfer.EMPTY_ARRAY );
    }

    public ConsumptionTransfer[] getConsumptions()
    {
        return (ConsumptionTransfer[]) this.consumptions.toArray( ConsumptionTransfer.EMPTY_ARRAY );
    }
}
