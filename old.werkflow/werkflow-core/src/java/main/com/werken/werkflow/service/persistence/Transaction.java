package com.werken.werkflow.service.persistence;

public interface Transaction
{
    CaseTransfer[] getModifiedCases();

    CorrelationTransfer[] getCorrelations();

    UncorrelatedTransfer[] getUncorrelateds();

    ConsumptionTransfer[] getConsumptions();
}
