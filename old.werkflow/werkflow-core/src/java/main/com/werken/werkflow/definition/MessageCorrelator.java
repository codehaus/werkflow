package com.werken.werkflow.definition;

import com.werken.werkflow.ProcessCase;

public interface MessageCorrelator
{
    boolean correlates(Object message,
                       ProcessCase processCase)
        throws Exception;
}
