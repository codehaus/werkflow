package org.codehaus.werkflow.expressions;

import org.codehaus.werkflow.Context;
import org.codehaus.werkflow.spi.Expression;

public class True
    implements Expression
{
    public static final True INSTANCE = new True();

    public boolean evaluateAsBoolean(Context context)
    {
        return true;
    }

    public Object evaluate(Context context)
    {
        return Boolean.TRUE;
    }

    public String toString()
    {
        return "true";
    }
}
