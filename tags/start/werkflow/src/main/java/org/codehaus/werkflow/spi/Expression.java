package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.Context;

public interface Expression
{
    boolean evaluateAsBoolean(Context context);
    Object evaluate(Context context);
}
