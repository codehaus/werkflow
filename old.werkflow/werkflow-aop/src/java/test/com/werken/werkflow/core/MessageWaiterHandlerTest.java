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

    public void testCorrelatesTrue()
        throws Exception
    {
        MessageType messageType = new MessageType( "message.type" );

        MockRegistration registration = new MockRegistration( messageType );

        DefaultNet net = new DefaultNet();

        DefaultTransition transition = net.addTransition( "the.transition" );

        MessageWaiter waiter = new MessageWaiter( messageType,
                                                  "message" );

        waiter.setMessageCorrelator( new MockMessageCorrelator( true ) );

        transition.setWaiter( waiter );

        MessageWaiterHandler handler = new MessageWaiterHandler( registration,
                                                                 transition );

        Message message = new DefaultMessage( "the.message",
                                              messageType,
                                              new Object() );

        ProcessCase processCase = new MockProcessCase( "the.case",
                                                       null );

        assertTrue( handler.correlates( processCase,
                                        message ) );
                                        
    }

    public void testCorrelatesFalse()
        throws Exception
    {
        MessageType messageType = new MessageType( "message.type" );

        MockRegistration registration = new MockRegistration( messageType );

        DefaultNet net = new DefaultNet();

        DefaultTransition transition = net.addTransition( "the.transition" );

        MessageWaiter waiter = new MessageWaiter( messageType,
                                                  "message" );

        waiter.setMessageCorrelator( new MockMessageCorrelator( false ) );

        transition.setWaiter( waiter );

        MessageWaiterHandler handler = new MessageWaiterHandler( registration,
                                                                 transition );

        Message message = new DefaultMessage( "the.message",
                                              messageType,
                                              new Object() );

        ProcessCase processCase = new MockProcessCase( "the.case",
                                                       null );

        assertFalse( handler.correlates( processCase,
                                        message ) );
    }

    public void testCorrelations_1_true()
        throws Exception
    {
        MessageType messageType = new MessageType( "message.type" );

        MockRegistration registration = new MockRegistration( messageType );

        DefaultNet net = new DefaultNet();

        DefaultTransition transition = net.addTransition( "the.transition" );

        MessageWaiter waiter = new MessageWaiter( messageType,
                                                  "message" );

        waiter.setMessageCorrelator( new MockMessageCorrelator( true ) );

        transition.setWaiter( waiter );

        MessageWaiterHandler handler = new MessageWaiterHandler( registration,
                                                                 transition );

        Message message = new DefaultMessage( "the.message",
                                              messageType,
                                              new Object() );

        CoreProcessCase processCase = new CoreProcessCase( null,
                                                           null,
                                                           null,
                                                           null,
                                                           null,
                                                           "the.case" );

        CoreChangeSet changeSet = new CoreChangeSet( null );

        handler.addCase( changeSet,
                         processCase );

        assertEquals( 0,
                      changeSet.getCoreCorrelations().length );

        handler.acceptMessage( changeSet,
                               message );

        assertEquals( 1,
                      changeSet.getCoreCorrelations().length );

        Correlation cor = changeSet.getCoreCorrelations()[0];

        assertEquals( "the.message",
                      cor.getMessageId() );

        assertEquals( "the.case",
                      cor.getCaseId() );

        assertEquals( "the.transition",
                      cor.getTransitionId() );
    }

    public void testCorrelations_1_false()
        throws Exception
    {
        MessageType messageType = new MessageType( "message.type" );

        MockRegistration registration = new MockRegistration( messageType );

        DefaultNet net = new DefaultNet();

        DefaultTransition transition = net.addTransition( "the.transition" );

        MessageWaiter waiter = new MessageWaiter( messageType,
                                                  "message" );

        waiter.setMessageCorrelator( new MockMessageCorrelator( false ) );

        transition.setWaiter( waiter );

        MessageWaiterHandler handler = new MessageWaiterHandler( registration,
                                                                 transition );

        Message message = new DefaultMessage( "the.message",
                                              messageType,
                                              new Object() );

        CoreProcessCase processCase = new CoreProcessCase( null,
                                                           null,
                                                           null,
                                                           null,
                                                           null,
                                                           "the.case" );

        CoreChangeSet changeSet = new CoreChangeSet( null );

        handler.addCase( changeSet,
                         processCase );

        assertEquals( 0,
                      changeSet.getCoreCorrelations().length );

        handler.acceptMessage( changeSet,
                               message );

        assertEquals( 0,
                      changeSet.getCoreCorrelations().length );
    }

    public void testCorrelations_2_true()
        throws Exception
    {
        MessageType messageType = new MessageType( "message.type" );

        MockRegistration registration = new MockRegistration( messageType );

        DefaultNet net = new DefaultNet();

        DefaultTransition transition = net.addTransition( "the.transition" );

        MessageWaiter waiter = new MessageWaiter( messageType,
                                                  "message" );

        waiter.setMessageCorrelator( new MockMessageCorrelator( true ) );

        transition.setWaiter( waiter );

        MessageWaiterHandler handler = new MessageWaiterHandler( registration,
                                                                 transition );

        Message message = new DefaultMessage( "the.message",
                                              messageType,
                                              new Object() );

        CoreProcessCase processCase = new CoreProcessCase( null,
                                                           null,
                                                           null,
                                                           null,
                                                           null,
                                                           "the.case" );

        CoreChangeSet changeSet = new CoreChangeSet( null );

        handler.acceptMessage( changeSet,
                               message );

        assertEquals( 0,
                      changeSet.getCoreCorrelations().length );

        handler.addCase( changeSet,
                         processCase );

        assertEquals( 1,
                      changeSet.getCoreCorrelations().length );

        Correlation cor = changeSet.getCoreCorrelations()[0];

        assertEquals( "the.message",
                      cor.getMessageId() );

        assertEquals( "the.case",
                      cor.getCaseId() );

        assertEquals( "the.transition",
                      cor.getTransitionId() );
    }

    public void testCorrelations_2_false()
        throws Exception
    {
        MessageType messageType = new MessageType( "message.type" );

        MockRegistration registration = new MockRegistration( messageType );

        DefaultNet net = new DefaultNet();

        DefaultTransition transition = net.addTransition( "the.transition" );

        MessageWaiter waiter = new MessageWaiter( messageType,
                                                  "message" );

        waiter.setMessageCorrelator( new MockMessageCorrelator( false ) );

        transition.setWaiter( waiter );

        MessageWaiterHandler handler = new MessageWaiterHandler( registration,
                                                                 transition );

        Message message = new DefaultMessage( "the.message",
                                              messageType,
                                              new Object() );

        CoreProcessCase processCase = new CoreProcessCase( null,
                                                           null,
                                                           null,
                                                           null,
                                                           null,
                                                           "the.case" );

        CoreChangeSet changeSet = new CoreChangeSet( null );

        handler.acceptMessage( changeSet,
                               message );

        assertEquals( 0,
                      changeSet.getCoreCorrelations().length );

        handler.addCase( changeSet,
                         processCase );

        assertEquals( 0,
                      changeSet.getCoreCorrelations().length );
    }

    public void testConsume()
        throws Exception
    {
        MessageType messageType = new MessageType( "message.type" );

        MockRegistration registration = new MockRegistration( messageType );

        DefaultNet net = new DefaultNet();

        DefaultTransition transition = net.addTransition( "the.transition" );

        MessageWaiter waiter = new MessageWaiter( messageType,
                                                  "message" );

        waiter.setMessageCorrelator( new MockMessageCorrelator( true ) );

        transition.setWaiter( waiter );

        MessageWaiterHandler handler = new MessageWaiterHandler( registration,
                                                                 transition );

        Message message = new DefaultMessage( "the.message",
                                              messageType,
                                              new Object() );

        registration.addMessage( message );

        CoreProcessCase processCase = new CoreProcessCase( null,
                                                           null,
                                                           null,
                                                           null,
                                                           null,
                                                           "the.case" );

        CoreChangeSet changeSet = new CoreChangeSet( null );

        handler.addCase( changeSet,
                         processCase );

        assertEquals( 0,
                      changeSet.getCoreCorrelations().length );

        handler.acceptMessage( changeSet,
                               message );

        assertEquals( 1,
                      changeSet.getCoreCorrelations().length );

        Message consumed = handler.consumeMessage( changeSet,
                                                   processCase,
                                                   "the.message" );

        assertSame( message,
                    consumed );

        try
        {
            handler.consumeMessage( changeSet,
                                    processCase,
                                    "the.message" );

            fail( "should have thrown NoSuchMessageException" );
        }
        catch (NoSuchMessageException e)
        {
            // expected and correct
        }
    }
}
