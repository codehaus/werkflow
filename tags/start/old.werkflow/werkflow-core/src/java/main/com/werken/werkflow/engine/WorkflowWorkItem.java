package com.werken.werkflow.engine;

import com.werken.werkflow.Attributes;
import com.werken.werkflow.activity.Activity;

public class WorkflowWorkItem
{
    private String caseId;
    private String transitionId;

    public WorkflowWorkItem(String caseId,
                            String transitionId)
    {
        this.caseId       = caseId;
        this.transitionId = transitionId;
    }

    public String getCaseId()
    {
        return this.caseId;
    }

    public String getTransitionId()
    {
        return this.transitionId;
    }

    public Activity fire(Attributes attributes)
    {
        return null;
    }
}
