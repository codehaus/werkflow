package com.werken.werkflow.core;

import com.werken.werkflow.Attributes;

interface ActivityCompleter
{
    void complete(CoreActivity activity,
                  Attributes caseAttrs);

    void completeWithError(CoreActivity activity,
                           Throwable error);
}
