package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.Context;

public interface SatisfactionManager
{
    public boolean isSatisfied(String satisfactionId,
                               Context context);

    public boolean isSatisfied(String satisfactionId,
                               Context context,
                               SatisfactionCallback callback);
}
