package com.werken.werkflow.expr;

public class MockExpression
    implements Expression
{
    private boolean result;
    private ExpressionContext context;

    public MockExpression(boolean result)
    {
        this.result = result;
    }

    public boolean evaluateAsBoolean(ExpressionContext context)
        throws Exception
    {
        this.context = context;

        return this.result;
    }

    public ExpressionContext getContext()
    {
        return this.context;
    }
}
