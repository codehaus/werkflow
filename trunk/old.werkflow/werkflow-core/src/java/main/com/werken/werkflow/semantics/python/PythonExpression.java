package com.werken.werkflow.semantics.python;

import com.werken.werkflow.Attributes;
import com.werken.werkflow.semantics.jelly.AttributesJellyContext;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.expression.Expression;

import org.python.core.PyInteger;
import org.python.core.PyString;
//import org.apache.commons.jexl.JexlContext;

public class PythonExpression
    implements com.werken.werkflow.expr.Expression
{
    private Expression expr;

    public PythonExpression(Expression expr)
    {
        this.expr = expr;
    }

    public boolean evaluateAsBoolean(Attributes attrs)
        throws Exception
    {
        JellyContext context = new AttributesJellyContext( attrs );

        Object value = this.expr.evaluate( context );

        if ( value instanceof PyInteger )
        {
            return ( ((PyInteger)value).getValue() != 0 );
        }

        if ( value instanceof PyString)
        {
            String str = ((PyString)value).toString();

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
