package org.codehaus.werkflow;

import org.codehaus.werkflow.spi.RobustTransaction;
import org.codehaus.werkflow.spi.SatisfactionCallback;

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

    public void notifySatisfied(Transaction transaction)
        throws Exception
    {
        this.engine.enqueueSatisfier( (RobustTransaction) transaction,
                                      this.instanceId,
                                      this.satisfactionId );
    }
}
