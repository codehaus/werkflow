package com.werken.werkflow.core;

interface CorrelationEvaluator
{
    void evaluate(CoreChangeSet changeSet,
                  CoreProcessCase processCase);
}
