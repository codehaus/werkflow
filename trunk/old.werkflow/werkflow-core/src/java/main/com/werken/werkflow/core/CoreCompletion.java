package com.werken.werkflow.core;

import com.werken.werkflow.Attributes;

class CoreCompletion
    implements Completion
{
    private ActivityCompleter completer;
    private CoreActivity activity;

    CoreCompletion(ActivityCompleter completer,
                   CoreActivity activity)
    {
        this.completer = completer;
        this.activity  = activity;
    }

    public void complete(Attributes caseAttrs)
    {
        this.completer.complete( this.activity,
                                 caseAttrs );
    }

    public void completeWithError(Throwable error)
    {
        this.completer.completeWithError( this.activity,
                                          error );
    }
}

