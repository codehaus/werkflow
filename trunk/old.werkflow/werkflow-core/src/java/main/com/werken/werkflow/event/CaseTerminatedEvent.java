package com.werken.werkflow.event;

import com.werken.werkflow.Wfms;

public class CaseTerminatedEvent
    extends CaseEvent
{

    public CaseTerminatedEvent(Wfms wfms,
                               String processId,
                               String caseId)
    {
        super( wfms,
               processId,
               caseId );
    }
}
