package com.werken.werkflow.definition.petri;

public class MockExpression
    implements Expression
{
    private boolean result;
    private Parameters parameters;

    public MockExpression(boolean result)
    {
        this.result = result;
    }

    public boolean evaluate(Parameters parameters)
        throws Exception
    {
        this.parameters = parameters;

        return this.result;
    }

    public Parameters getParameters()
    {
        return this.parameters;
    }
}
