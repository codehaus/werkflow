package com.werken.werkflow.core;

import com.werken.werkflow.Attributes;

class MockActivityCompleter
    implements ActivityCompleter
{
    private CoreActivity activity;
    private Attributes caseAttrs;
    private Throwable error;

    MockActivityCompleter()
    {
    }

    public void complete(CoreActivity activity,
                         Attributes caseAttrs)
    {
        this.activity  = activity;
        this.caseAttrs = caseAttrs;
    }

    public void completeWithError(CoreActivity activity,
                                  Throwable error)
    {
        this.activity = activity;
        this.error    = error;
    }

    CoreActivity getActivity()
    {
        return this.activity;
    }

    Attributes getCaseAttributes()
    {
        return this.caseAttrs;
    }

    Throwable getError()
    {
        return this.error;
    }
}
