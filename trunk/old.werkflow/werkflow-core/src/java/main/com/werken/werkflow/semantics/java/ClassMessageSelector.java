package com.werken.werkflow.semantics.java;

import  com.werken.werkflow.service.messaging.simple.SimpleMessageSelector;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.expression.Expression;

public class ClassMessageSelector
    extends SimpleMessageSelector
{
    private Class messageClass;
    private Expression expression;

    public ClassMessageSelector(Class messageClass)
    {
        this( messageClass,
              null );
    }

    public ClassMessageSelector(Class messageClass,
                                Expression expression)
    {
        this.messageClass = messageClass;
        this.expression   = expression;
    }

    public Class getMessageClass()
    {
        return this.messageClass;
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    public boolean selects(Object message)
    {
        boolean selects = getMessageClass().isInstance( message );

        if ( ! selects )
        {
            return false;
        }

        if ( getExpression() == null )
        {
            return true;
        }

        JellyContext context = new JellyContext();

        context.setVariable( "message",
                             message );

        return getExpression().evaluateAsBoolean( context );
    }
}
