package com.werken.werkflow.admin;

import com.werken.werkflow.definition.ProcessDefinition;

public class ProcessVerificationException
    extends ProcessException
{
    public ProcessVerificationException(ProcessDefinition processDef)
    {
        super( processDef );
    }
}
