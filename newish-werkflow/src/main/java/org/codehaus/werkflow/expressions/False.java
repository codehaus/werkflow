package org.codehaus.werkflow;

import org.codehaus.werkflow.Context;
import org.codehaus.werkflow.spi.Expression;

public class False
    implements Expression
{
    public static final False INSTANCE = new False();

    public boolean evaluate(Context context)
    {
        return false;
    }

    public String toString()
    {
        return "false";
    }
}
