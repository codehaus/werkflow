package com.werken.werkflow.semantics.jexl;

import com.werken.werkflow.Attributes;

import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.JexlContext;

public class JexlExpression
    implements com.werken.werkflow.expr.Expression
{
    private Expression expr;

    public JexlExpression(Expression expr)
    {
        this.expr = expr;
    }

    public boolean evaluateAsBoolean(Attributes attrs)
        throws Exception
    {
        JexlContext context = new AttributesJexlContext( attrs );

        Object value = this.expr.evaluate( context );

        if ( value instanceof Boolean )
        {
            return ((Boolean)value).booleanValue();
        }

        if ( value instanceof String)
        {
            String str = (String) value;

            if ( str.equals( "true" )
                 ||
                 str.equals( "on" )
                 ||
                 str.equals( "1" )
                 ||
                 str.equals( "yes" ) )
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        return false;
    }
}
