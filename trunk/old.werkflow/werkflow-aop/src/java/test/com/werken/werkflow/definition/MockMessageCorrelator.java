package com.werken.werkflow.definition;

import com.werken.werkflow.ProcessCase;

public class MockMessageCorrelator
    implements MessageCorrelator
{
    private boolean result;

    public MockMessageCorrelator(boolean result)
    {
        this.result = result;
    }

    public boolean correlates(Object message,
                              ProcessCase processCase)
    {
        return this.result;
    }
}
