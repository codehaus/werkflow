package com.werken.werkflow.semantics.jexl;

import com.werken.werkflow.expr.ExpressionContext;

import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;

import java.util.Map;
import java.util.HashMap;

public class JexlExpression
    implements com.werken.werkflow.expr.Expression
{
    private Expression expr;

    public JexlExpression(Expression expr)
    {
        this.expr = expr;
    }

    public boolean evaluateAsBoolean(ExpressionContext exprContext)
        throws Exception
    {
        JexlContext context = JexlHelper.createContext();

        Map varsMap = new HashMap();

        String[] names = exprContext.getNames();

        for ( int i = 0 ; i < names.length ; ++i )
        {
            varsMap.put( names[i],
                         exprContext.getValue( names[i] ) );
        }

        context.setVars( varsMap );

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
