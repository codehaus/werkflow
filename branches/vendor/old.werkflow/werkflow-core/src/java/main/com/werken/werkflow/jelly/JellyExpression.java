package com.werken.werkflow.jelly;

import com.werken.werkflow.definition.petri.Parameters;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.expression.Expression;


public class JellyExpression
    implements com.werken.werkflow.definition.petri.Expression
{
    private Expression expr;

    public JellyExpression(Expression expr)
    {
        this.expr = expr;
    }

    public Expression getExpression()
    {
        return this.expr;
    }

    public boolean evaluate(Parameters params)
        throws Exception
    {
        boolean result = false;

        String[] paramNames = params.getParameterNames();

        JellyContext context = new JellyContext();

        for ( int i = 0 ; i < paramNames.length ; ++i )
        {
            context.setVariable( paramNames[i],
                                 params.getParameter( paramNames[i] ) );
        }

        return getExpression().evaluateAsBoolean( context );
    }
}
