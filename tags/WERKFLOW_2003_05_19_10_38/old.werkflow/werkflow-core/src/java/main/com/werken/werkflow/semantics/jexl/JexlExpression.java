package com.werken.werkflow.semantics.jexl;

import com.werken.werkflow.expr.AbstractExpression;
import com.werken.werkflow.expr.ExpressionContext;

import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;

import java.util.Map;
import java.util.HashMap;

public class JexlExpression
    extends AbstractExpression
{
    private Expression expr;

    public JexlExpression(Expression expr)
    {
        this.expr = expr;
    }

    public Object evaluate(ExpressionContext exprContext)
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

        return this.expr.evaluate( context );
    }
}
