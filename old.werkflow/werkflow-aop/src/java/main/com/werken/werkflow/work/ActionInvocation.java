package com.werken.werkflow.work;

import com.werken.werkflow.MutableAttributes;

public interface ActionInvocation
{
    String getPackageId();

    String getProcessId();

    String getCaseId();

    MutableAttributes getCaseAttributes();

    MutableAttributes getOtherAttributes();

    void complete();

    void completeWithError(Throwable error);
}
