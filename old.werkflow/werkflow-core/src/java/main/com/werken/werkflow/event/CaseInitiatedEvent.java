package com.werken.werkflow.event;

import com.werken.werkflow.Wfms;

public class CaseInitiatedEvent
    extends CaseEvent
{

    public CaseInitiatedEvent(Wfms wfms,
                              String processId,
                              String caseId)
    {
        super( wfms,
               processId,
               caseId );
    }
}
