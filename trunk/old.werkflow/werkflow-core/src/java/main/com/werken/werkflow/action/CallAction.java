package com.werken.werkflow.action;

import com.werken.werkflow.Attributes;

public interface CallAction
    extends Action
{
    String getProcessId();

    Attributes getAttributes();
}
