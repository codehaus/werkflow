package com.werken.werkflow.core;

class MockCaseEvaluator
    implements CaseEvaluator
{
    private CoreProcessCase evaluated;
    private CoreWorkItem[] workItems;

    MockCaseEvaluator()
    {
        this.workItems = CoreWorkItem.EMPTY_ARRAY;
    }

    MockCaseEvaluator(CoreWorkItem[] workItems)
    {
        this.workItems = workItems;
    }

    public CoreWorkItem[] evaluate(CoreChangeSet changeSet,
                                   CoreProcessCase processCase)
    {
        this.evaluated = processCase;

        return this.workItems;
    }

    CoreProcessCase getEvaluated()
    {
        return this.evaluated;
    }
}
