package org.codehaus.werkflow.helpers;

import org.codehaus.werkflow.Context;
import org.codehaus.werkflow.spi.SatisfactionManager;
import org.codehaus.werkflow.spi.SatisfactionCallback;

public class SimpleSatisfactionManager
    implements SatisfactionManager
{
    public boolean isSatisfied(String satisfactionId,
                               Context context,
                               SatisfactionCallback callback)
    {
        return false;
    }
}
