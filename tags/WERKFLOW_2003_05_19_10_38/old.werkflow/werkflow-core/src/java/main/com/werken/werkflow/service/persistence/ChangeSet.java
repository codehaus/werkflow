package com.werken.werkflow.service.persistence;

public interface ChangeSet
{
    CaseTransfer[] getModifiedCases();

    CorrelationTransfer[] getCorrelations();

    UncorrelatedTransfer[] getUncorrelateds();

    ConsumptionTransfer[] getConsumptions();
}
