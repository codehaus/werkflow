package com.werken.werkflow.semantics.ognl;

import com.werken.werkflow.expr.Expression;

public class OgnlExpressionFactory
    implements com.werken.werkflow.expr.ExpressionFactory
{
    private static final OgnlExpressionFactory INSTANCE = new OgnlExpressionFactory();

    public static OgnlExpressionFactory getInstance()
    {
        return INSTANCE;
    }

    public OgnlExpressionFactory()
    {
    }

    public Expression newExpression(String exprStr)
        throws Exception
    {
        if ( exprStr == null )
        {
            return null;
        }

        return new OgnlExpression( exprStr );
    }
}
