package com.werken.werkflow.service.persistence;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class DefaultCaseTransfer
    implements CaseTransfer
{
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private String caseId;
    private Map attributes;
    private String[] tokens;

    public DefaultCaseTransfer(String caseId)
    {
        this( caseId,
              Collections.EMPTY_MAP,
              EMPTY_STRING_ARRAY );
    }

    public DefaultCaseTransfer(String caseId,
                               Map attributes,
                               String[] tokens)
    {
        this.caseId     = caseId;
        this.attributes = new HashMap( attributes );
        this.tokens     = tokens;
    }

    public String getCaseId()
    {
        return this.caseId;
    }

    public Map getAttributes()
    {
        return this.attributes;
    }

    public String[] getTokens()
    {
        return this.tokens;
    }
}
