package org.codehaus.werkflow.expressions;

import org.codehaus.werkflow.Context;
import org.codehaus.werkflow.spi.Expression;

public class Literal
    implements Expression
{
    private Object value;

    public Literal(Object value)
    {
        this.value = value;
    }

    public Object getValue()
    {
        return this.value;
    }

    public boolean evaluateAsBoolean(Context context)
    {
        if ( this.value == null )
        {
            return false;
        }

        return true;
    }

    public Object evaluate(Context context)
    {
        return this.value;
    }

    public String toString()
    {
        return this.value.toString();
    }
}
