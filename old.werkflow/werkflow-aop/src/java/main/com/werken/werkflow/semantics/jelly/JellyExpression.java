package com.werken.werkflow.semantics.jelly;

import com.werken.werkflow.Attributes;

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

    public boolean evaluate(Attributes attrs)
        throws Exception
    {
        boolean result = false;

        String[] attrNames = attrs.getAttributeNames();

        JellyContext context = new JellyContext();

        for ( int i = 0 ; i < attrNames.length ; ++i )
        {
            context.setVariable( attrNames[i],
                                 attrs.getAttribute( attrNames[i] ) );
        }

        return getExpression().evaluateAsBoolean( context );
    }
}
