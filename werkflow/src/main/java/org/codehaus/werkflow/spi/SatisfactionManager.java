package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.Context;

public interface SatisfactionManager
{
    boolean isSatisfied(RobustTransaction transaction,
                        String satisfactionId,
                        Context context,
                        SatisfactionCallback callback);

    SatisfactionValues getSatisfactionValues(String satisfactionId,
                                             Context context);

    void satisfy(RobustTransaction transaction,
                 String instanceId,
                 String satisfactionId,
                 SatisfactionValues values)
        throws Exception;
}
