package com.werken.werkflow.semantics.java;

import com.werken.werkflow.expr.Expression;

public class JavaExpressionFactory
    implements com.werken.werkflow.expr.ExpressionFactory
{
    private static final JavaExpressionFactory INSTANCE = new JavaExpressionFactory();

    public static JavaExpressionFactory getInstance()
    {
        return INSTANCE;
    }

    public JavaExpressionFactory()
    {
    }

    public Expression newExpression(String exprStr)
        throws Exception
    {
        if ( exprStr == null )
        {
            return null;
        }

        return new JavaExpression( exprStr );
    }
}
