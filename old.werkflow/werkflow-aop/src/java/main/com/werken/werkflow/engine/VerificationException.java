package com.werken.werkflow.engine;

public class VerificationException
    extends EngineException
{
    private WorkflowProcessCase processCase;

    public VerificationException(WorkflowProcessCase processCase)
    {
        this.processCase = processCase;
    }

    public VerificationException(WorkflowProcessCase processCase,
                                 Throwable rootCause)
    {
        super( rootCause );

        this.processCase = processCase;
    }

    public WorkflowProcessCase getProcessCase()
    {
        return this.processCase;
    }
}
