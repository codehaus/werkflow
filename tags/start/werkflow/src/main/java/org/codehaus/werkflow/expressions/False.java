package org.codehaus.werkflow.expressions;

import org.codehaus.werkflow.Context;
import org.codehaus.werkflow.spi.Expression;

public class False
    implements Expression
{
    public static final False INSTANCE = new False();

    public boolean evaluateAsBoolean(Context context)
    {
        return false;
    }

    public Object evaluate(Context context)
    {
        return Boolean.FALSE;
    }

    public String toString()
    {
        return "false";
    }
}
