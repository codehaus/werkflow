package com.werken.werkflow.engine;

public class NoSuchCorrelationException
extends EngineException
{
    private String processCaseId;
    private String transitionId;

    public NoSuchCorrelationException(String processCaseId,
                                      String transitionId)
    {
        this.processCaseId = processCaseId;
        this.transitionId  = transitionId;
    }

    public String getProcessCaseId()
    {
        return this.processCaseId;
    }

    public String getTransitionId()
    {
        return this.transitionId;
    }
}
