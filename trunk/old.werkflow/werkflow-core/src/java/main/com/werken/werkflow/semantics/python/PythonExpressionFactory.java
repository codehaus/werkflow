package com.werken.werkflow.semantics.python;

import com.werken.werkflow.expr.Expression;

public class PythonExpressionFactory
    implements com.werken.werkflow.expr.ExpressionFactory
{
    private static final PythonExpressionFactory INSTANCE = new PythonExpressionFactory();

    public static PythonExpressionFactory getInstance()
    {
        return INSTANCE;
    }

    public PythonExpressionFactory()
    {
    }

    public Expression newExpression(String exprStr)
        throws Exception
    {
        return new PythonExpression( exprStr );
    }
}
