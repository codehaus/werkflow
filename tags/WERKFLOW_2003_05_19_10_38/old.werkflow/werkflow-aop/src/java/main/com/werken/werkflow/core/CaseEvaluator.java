package com.werken.werkflow.core;

interface CaseEvaluator
{
    CoreWorkItem[] evaluate(CoreChangeSet changeSet,
                            CoreProcessCase processCase);
}
