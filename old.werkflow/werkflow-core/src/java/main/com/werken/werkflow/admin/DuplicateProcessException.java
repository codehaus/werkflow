package com.werken.werkflow.admin;

import com.werken.werkflow.definition.ProcessDefinition;

public class DuplicateProcessException
    extends ProcessException
{
    public DuplicateProcessException(ProcessDefinition processDef)
    {
        super( processDef );
    }
}
