package com.werken.werkflow.engine;

import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.service.messaging.Registration;

import java.util.List;
import java.util.LinkedList;

public class Correlator
{
    private MessageType messageType;
    private Registration registration;
    private List messages;
    private List blockers;

    public Correlator(MessageType messageType)
    {
        this.messageType = messageType;

        this.messages = new LinkedList();
        this.blockers = new LinkedList();
    }

    public MessageType getMessageType()
    {
        return this.messageType;
    }

    public void setRegistration(Registration registration)
    {
        this.registration = registration;
    }

    public Registration getRegistration()
    {
        return this.registration;
    }

    public void addMessage(Object message)
    {
        if ( ! checkCorrelationsNewMessage( message ) )
        {
            this.messages.add( message );
        }
    }

    public void removeMessage(Object message)
    {
        while ( this.messages.remove( message ) )
        {
            // intentionally left blank
        }
    }

    public void addBlocker(WorkflowProcessCase blocker)
    {
        if ( ! checkCorrelationsNewBlocker( blocker ) )
        {
            this.blockers.add( blocker );
        }
    }

    public void removeBlocker(WorkflowProcessCase blocker)
    {
        while ( this.blockers.remove( blocker ) )
        {
            // intentionally left blank
        }
    }

    protected boolean checkCorrelationsNewMessage(Object message)
    {
        return false;
    }

    protected boolean checkCorrelationsNewBlocker(WorkflowProcessCase blocker)
    {
        return false;
    }
}
