package com.werken.werkflow.action;

import com.werken.werkflow.Attributes;
import com.werken.werkflow.ProcessCase;

public interface CallAction
    extends Action
{
    String getPackageId();

    String getProcessId();

    Attributes getAttributes(ProcessCase parentCase)
        throws Exception;
}
