package org.codehaus.werkflow;

import org.codehaus.werkflow.spi.SatisfactionCallback;
import org.codehaus.werkflow.spi.SatisfactionValues;

class EngineSatisfactionCallback
    implements SatisfactionCallback
{
    private Engine engine;
    private String instanceId;
    private String satisfactionId;

    EngineSatisfactionCallback(Engine engine,
                               String instanceId,
                               String satisfactionId)
    {
        this.engine         = engine;
        this.instanceId     = instanceId;
        this.satisfactionId = satisfactionId;
    }

    public void notifySatisfied(SatisfactionValues values)
        throws Exception
    {
        this.engine.satisfy( null,
                             this.instanceId,
                             this.satisfactionId,
                             values );
    }
}
