package com.werken.werkflow.core;

import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.MockProcessCase;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.MockMessageCorrelator;
import com.werken.werkflow.definition.petri.DefaultNet;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.service.messaging.MockRegistration;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.DefaultMessage;
import com.werken.werkflow.service.messaging.NoSuchMessageException;

import junit.framework.TestCase;

public class MessageWaiterHandlerTest
    extends TestCase
{
    public void testConstruct()
        throws Exception
    {
        MessageType messageType = new MessageType( "message.type" );

        MockRegistration registration = new MockRegistration( messageType );

        DefaultNet net = new DefaultNet();

        DefaultTransition transition = net.addTransition( "the.transition" );

        MessageWaiterHandler handler = new MessageWaiterHandler( registration,
                                                                 transition );

        assertSame( registration,
                    handler.getRegistration() );

        assertSame( transition,
                    handler.getTransition() );
    }
}
