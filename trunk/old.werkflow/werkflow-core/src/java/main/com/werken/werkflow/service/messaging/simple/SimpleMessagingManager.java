package com.werken.werkflow.service.messaging.simple;

import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.service.messaging.MessagingManager;
import com.werken.werkflow.service.messaging.MessageSink;
import com.werken.werkflow.service.messaging.MessageSelector;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.IncompatibleMessageSelectorException;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class SimpleMessagingManager
    implements MessagingManager
{
    private List registrations;

    public SimpleMessagingManager()
    {
        this.registrations = new LinkedList();
    }

    public Registration register(MessageSink sink,
                                 MessageType messageType)
        throws IncompatibleMessageSelectorException
    {
        MessageSelector selector = messageType.getMessageSelector();

        if ( ! ( selector instanceof SimpleMessageSelector ) )
        {
            throw new IncompatibleMessageSelectorException( selector );
        }

        SimpleRegistration registration = new SimpleRegistration( this,
                                                                  sink,
                                                                  messageType );

        this.registrations.add( registration );

        return registration;
    }

    public void unregister(SimpleRegistration registration)
    {
        while ( this.registrations.remove( registration ) )
        {
            // intentionally left blank
        }
    }

    public void acceptMessage(Object message)
    {
        Iterator           regIter = this.registrations.iterator();
        SimpleRegistration eachReg = null;

        while ( regIter.hasNext() )
        {
            eachReg = (SimpleRegistration) regIter.next();

            eachReg.acceptMessage( message );
        }
    }
}
