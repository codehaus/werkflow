package com.werken.werkflow.definition.petri;

import com.werken.werkflow.Attributes;

public class MockExpression
    implements Expression
{
    private boolean result;
    private Attributes attributes;

    public MockExpression(boolean result)
    {
        this.result = result;
    }

    public boolean evaluate(Attributes attributes)
        throws Exception
    {
        this.attributes = attributes;

        return this.result;
    }

    public Attributes getAttributes()
    {
        return this.attributes;
    }
}
