package com.werken.werkflow.service.messaging.simple;

import com.werken.werkflow.service.messaging.MockMessageSink;

import junit.framework.TestCase;

public class SimpleMessagingManagerTest
    extends TestCase
{
    public void testConstruct()
    {
        SimpleMessagingManager manager = new SimpleMessagingManager();

        MockMessageSink sink = new MockMessageSink();
    }
}
