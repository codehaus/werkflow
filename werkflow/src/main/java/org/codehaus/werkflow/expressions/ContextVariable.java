package org.codehaus.werkflow.expressions;

import org.codehaus.werkflow.Context;
import org.codehaus.werkflow.spi.Expression;

public class ContextVariable
    implements Expression
{

    private String id;

    public ContextVariable(String id)
    {
        this.id = id;
    }

    public boolean evaluateAsBoolean(Context context)
    {
        return ( context.get( this.id ) != null );
    }

    public Object evaluate(Context context)
    {
        return context.get( this.id );
    }

    public String toString()
    {
        return "true";
    }
}
