package com.werken.werkflow.core;

import com.werken.werkflow.WerkflowTestCase;
import com.werken.werkflow.definition.petri.DefaultNet;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.service.messaging.MockRegistration;

public class CorrelationMessageHandlerTest
    extends WerkflowTestCase
{
    public void testConstruct()
    {
        MockRegistration registration = new MockRegistration( null );

        CorrelationMessageHandler handler = new CorrelationMessageHandler( registration );

        assertSame( registration,
                    handler.getRegistration() );
    }

    public void testAdd()
        throws Exception
    {
        DefaultNet net = new DefaultNet();

        DefaultTransition transition = net.addTransition( "the.transition" );

        MockRegistration registration = new MockRegistration( null );

        CorrelationMessageHandler handler = new CorrelationMessageHandler( registration );

        handler.add( transition );

        assertNotNull( handler.getMessageWaiterHandler( "the.transition" ) );
    }
}
