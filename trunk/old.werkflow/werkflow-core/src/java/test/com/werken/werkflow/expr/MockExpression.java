package com.werken.werkflow.expr;

public class MockExpression
    extends AbstractExpression
{
    private Object result;
    private ExpressionContext context;

    public MockExpression(Object result)
    {
        this.result = result;
    }

    public MockExpression(boolean result)
    {
        if ( result )
        {
            this.result = Boolean.TRUE;
        }
        else
        {
            this.result = Boolean.FALSE;
        }
    }

    public Object evaluate(ExpressionContext context)
        throws Exception
    {
        return this.result;
    }

    public ExpressionContext getContext()
    {
        return this.context;
    }
}
