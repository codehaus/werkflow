package com.werken.werkflow.core;

import com.werken.werkflow.service.messaging.Message;

class CoreActivity
{
    static final CoreActivity[] EMPTY_ARRAY = new CoreActivity[0];
    
    private CoreWorkItem workItem;
    private Message message;

    CoreActivity(CoreWorkItem workItem,
                 Message message)
    {
        this.workItem = workItem;
        this.message  = message;
    }

    CoreWorkItem getWorkItem()
    {
        return this.workItem;
    }

    Message getMessage()
    {
        return this.message;
    }
}
