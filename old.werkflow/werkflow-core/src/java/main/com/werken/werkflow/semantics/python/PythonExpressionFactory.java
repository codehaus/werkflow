package com.werken.werkflow.semantics.python;

import com.werken.werkflow.expr.Expression;

import org.apache.commons.jelly.tags.bsf.BSFExpressionFactory;

import org.apache.commons.jexl.ExpressionFactory;

public class PythonExpressionFactory
    implements com.werken.werkflow.expr.ExpressionFactory
{
    private BSFExpressionFactory factory;

    public PythonExpressionFactory()
    {
        this.factory = new BSFExpressionFactory();
        this.factory.setLanguage( "jython" );
    }

    public Expression newExpression(String exprStr)
        throws Exception
    {
        //return new JexlExpression( ExpressionFactory.createExpression( expr ) );
        Expression expr = new PythonExpression( this.factory.createExpression( exprStr ) );

        System.err.println( "new expr [" + exprStr + "] " + expr );

        return expr;
    }
}
