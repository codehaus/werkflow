package com.werken.werkflow.semantics.java;

import  com.werken.werkflow.service.messaging.simple.SimpleMessageSelector;

public class ClassMessageSelector
    extends SimpleMessageSelector
{
    private Class messageClass;

    public ClassMessageSelector(Class messageClass)
    {
        this.messageClass = messageClass;
    }

    public Class getMessageClass()
    {
        return this.messageClass;
    }

    public boolean selects(Object message)
    {
        return getMessageClass().isInstance( message );
    }
}
