package com.werken.werkflow.service.messaging.simple;

import com.werken.werkflow.service.messaging.MessageSelector;

public abstract class SimpleMessageSelector
    implements MessageSelector
{
    public SimpleMessageSelector()
    {

    }

    public abstract boolean selects(Object message);
}
