package com.werken.werkflow.semantics.jexl;

import com.werken.werkflow.expr.Expression;

import org.apache.commons.jexl.ExpressionFactory;

public class JexlExpressionFactory
    implements com.werken.werkflow.expr.ExpressionFactory
{
    public JexlExpressionFactory()
    {

    }

    public Expression newExpression(String expr)
        throws Exception
    {
        System.err.println( "JEXL " + expr );
        return new JexlExpression( ExpressionFactory.createExpression( expr ) );
    }
}
