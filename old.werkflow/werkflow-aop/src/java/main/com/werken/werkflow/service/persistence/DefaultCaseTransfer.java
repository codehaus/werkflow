package com.werken.werkflow.service.persistence;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class DefaultCaseTransfer
{
    private String caseId;
    private Map attributes;

    public DefaultCaseTransfer(String caseId)
    {
        this( caseId,
              Collections.EMPTY_MAP );
    }

    public DefaultCaseTransfer(String caseId,
                               Map attributes)
    {
        this.attributes = new HashMap( attributes );
    }

    public String getCaseId()
    {
        return this.caseId;
    }

    public Map getAttributes()
    {
        return this.attributes;
    }
}
