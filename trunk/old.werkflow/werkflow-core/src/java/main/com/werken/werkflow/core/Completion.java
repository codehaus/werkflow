package com.werken.werkflow.core;

import com.werken.werkflow.Attributes;

interface Completion
{
    void complete(Attributes caseAttrs);

    void completeWithError(Throwable erorr);

}

