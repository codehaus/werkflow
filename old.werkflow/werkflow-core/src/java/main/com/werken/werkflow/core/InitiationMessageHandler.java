package com.werken.werkflow.core;

import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.NoSuchMessageException;
import com.werken.werkflow.service.persistence.PersistenceException;

class InitiationMessageHandler
    implements TerminalMessageHandler
{
    private Registration registration;
    private Transition transition;
    private ProcessDeployment deployment;

    public InitiationMessageHandler(Registration registration,
                                    Transition transition,
                                    ProcessDeployment deployment)
    {
        this.registration = registration;
        this.transition   = transition;
        this.deployment   = deployment;
    }

    public boolean acceptMessage(Message message)
    {
        try
        {
            ProcessCase newCase = this.deployment.initiate( this.transition,
                                                            message.getId() );
            
            return ( newCase != null );
        }
        catch (PersistenceException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public Message consumeMessage(CoreChangeSet changeSet,
                                  CoreProcessCase processCase,
                                  String transitionId,
                                  String messageId)
        throws NoSuchMessageException
    {
        return this.registration.consumeMessage( messageId );
    }

    public void add(Transition transition)
    {

    }

    public boolean addCase(CoreProcessCase processCase,
                           String transitionId)
    {
        return false;
    }

    public void removeCase(CoreProcessCase processCase,
                           String transitionId)

    {
        // intentionally left blank
    }
}
