package com.werken.werkflow.semantics.python;

import com.werken.werkflow.expr.Expression;

import org.apache.commons.jelly.tags.bsf.BSFExpressionFactory;

import org.apache.commons.jexl.ExpressionFactory;

public class PythonExpressionFactory
    implements com.werken.werkflow.expr.ExpressionFactory
{
    private static final PythonExpressionFactory INSTANCE = new PythonExpressionFactory();

    public static PythonExpressionFactory getInstance()
    {
        return INSTANCE;
    }

    private BSFExpressionFactory factory;

    public PythonExpressionFactory()
    {
        this.factory = new BSFExpressionFactory();
        this.factory.setLanguage( "jython" );
    }

    public Expression newExpression(String exprStr)
        throws Exception
    {
        return new PythonExpression( this.factory.createExpression( exprStr ) );
    }
}