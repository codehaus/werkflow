package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.Context;

public interface Expression
{
    boolean evaluate(Context context);
}
