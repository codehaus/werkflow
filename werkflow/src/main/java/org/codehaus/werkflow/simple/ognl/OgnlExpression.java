package org.codehaus.werkflow.simple.ognl;

import org.codehaus.werkflow.Context;
import org.codehaus.werkflow.spi.Expression;

import ognl.Ognl;
import ognl.OgnlException;

class OgnlExpression
    implements Expression
{
    private String expression;

    OgnlExpression(String expression)
    {
        this.expression = expression;
    }

    public String getExpression()
    {
        return this.expression;
    }

    public Object evaluate(Context context)
    {
        try
        {
            return Ognl.getValue( this.expression,
                                  context.getContextMap(),
                                  (Object) context);
        }
        catch (OgnlException e)
        {
            return null;
        }
    }

    public boolean evaluateAsBoolean(Context context)
    {
        Object result = evaluate( context );

        if ( result instanceof Boolean )
        {
            return ((Boolean)result).booleanValue();
        }

        return false;
    }

    public String toString()
    {
        return this.expression.toString();
    }
}
