package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.NoSuchInstanceException;

public interface SatisfactionCallback
{
    void notifySatisfied(SatisfactionValues values)
        throws Exception;
}
