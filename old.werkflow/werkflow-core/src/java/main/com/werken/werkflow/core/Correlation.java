package com.werken.werkflow.core;

import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.messaging.Message;

class Correlation
{
    public static final Correlation[] EMPTY_ARRAY = new Correlation[0];

    private String caseId;
    private String transitionId;
    private String messageId;

    Correlation(CoreProcessCase processCase,
                Transition transition,
                Message message)
    {
        this.caseId       = processCase.getId();
        this.transitionId = transition.getId();
        this.messageId    = message.getId();
    }

    Correlation(String caseId,
                String transitionId,
                String messageId)
    {
        this.caseId       = caseId;
        this.transitionId = transitionId;
        this.messageId    = messageId;
    }

    String getTransitionId()
    {
        return this.transitionId;
    }

    String getCaseId()
    {
        return this.caseId;
    }

    String getMessageId()
    {
        return this.messageId;
    }
}
