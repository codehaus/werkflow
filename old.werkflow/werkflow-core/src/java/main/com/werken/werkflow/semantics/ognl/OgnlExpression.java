package com.werken.werkflow.semantics.ognl;

import com.werken.werkflow.expr.Expression;
import com.werken.werkflow.expr.AbstractExpression;
import com.werken.werkflow.expr.ExpressionContext;

import ognl.Ognl;
import ognl.OgnlException;

import java.util.Map;
import java.util.HashMap;

public class OgnlExpression
    extends AbstractExpression
{
    private String text;
    private Object ast;

    public OgnlExpression(String text)
        throws OgnlException
    {
        this.text = text;
        this.ast = Ognl.parseExpression( text );
    }

    public String getText()
    {
        return this.text;
    }

    public boolean evaluateAsBoolean(ExpressionContext context)
        throws Exception
    {
        Map exprContext = new HashMap();

        String[] names = context.getNames();

        for ( int i = 0 ; i < names.length ; ++i )
        {
            exprContext.put( names[i],
                             context.getValue( names[i] ) );
        }

        Object value = Ognl.getValue( this.ast,
                                      exprContext,
                                      (Object) null );

        if ( value instanceof Boolean )
        {
            return ((Boolean)value).booleanValue();
        }

        if ( value instanceof String )
        {
            return stringAsBoolean( (String) value );
        }
                                      
        return false;
    }
}
