package com.werken.werkflow.engine;

import com.werken.werkflow.SimpleAttributes;
import com.werken.werkflow.NoSuchProcessException;
import com.werken.werkflow.action.Action;
import com.werken.werkflow.definition.MessageInitiator;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.MessageSink;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.NoSuchMessageException;
import com.werken.werkflow.service.messaging.IncompatibleMessageSelectorException;

import java.util.Map;
import java.util.HashMap;

public class Initiator
    implements MessageSink
{
    private WorkflowEngine engine;
    private ProcessDeployment deployment;
    private Map messageInitiators;
    private Map registrations;

    public Initiator(WorkflowEngine engine,
                     ProcessDeployment deployment)
    {
        this.engine     = engine;
        this.deployment = deployment;

        this.messageInitiators = new HashMap();
        this.registrations     = new HashMap();
    }

    public WorkflowEngine getEngine()
    {
        return this.engine;
    }

    public ProcessDeployment getProcessDeployment()
    {
        return this.deployment;
    }

    public void addMessageInitiators(MessageInitiator[] messageInitiators)
        throws ProcessDeploymentException
    {
        try
        {
            for ( int i = 0 ; i < messageInitiators.length ; ++i )
            {
                addMessageInitiator( messageInitiators[i] );
            }
        }
        catch (IncompatibleMessageSelectorException e)
        {
            // FIXME: unregister thingies.
            e.printStackTrace();
            throw new ProcessDeploymentException( e );
        }
    }

    public void addMessageInitiator(MessageInitiator messageInitiator)
        throws IncompatibleMessageSelectorException
    {
        MessageType messageType = messageInitiator.getMessageType();

        Registration registration = getEngine().register( this,
                                                          messageType );

        this.registrations.put( messageType,
                                registration );

        this.messageInitiators.put( messageType,
                                    messageInitiator );
    }

    public MessageInitiator getMessageInitiator(MessageType messageType)
    {
        return (MessageInitiator) this.messageInitiators.get( messageType );
    }

    public Registration getRegistration(MessageType messageType)
    {
        return (Registration) this.registrations.get( messageType );
    }

    public void acceptMessage(Message message)
    {
        MessageType messageType = message.getMessageType();

        MessageInitiator initiator = getMessageInitiator( messageType );

        if ( initiator != null )
        {
            Registration registration = getRegistration( messageType );

            try
            {
                registration.consumeMessage( message.getId() );

                Map caseAttrs = new HashMap();
                Map otherAttrs = new HashMap();

                Action action = initiator.getAction();

                if ( action != null )
                {
                    caseAttrs.put( initiator.getBindingVar(),
                                   message.getMessage() );

                    otherAttrs.put( initiator.getBindingVar(),
                                    message.getMessage() );

                    InitiatorActivity activity = new InitiatorActivity();

                    try
                    {
                        action.perform( activity,
                                        caseAttrs,
                                        otherAttrs );
                    }
                    catch (Exception e)
                    {

                    }
                }

                
                SimpleAttributes initialAttrs = new SimpleAttributes( caseAttrs );

                /*
                attrs.setAttribute( initiator.getBindingVar(),
                                    message.getMessage() );
                */
                
                getEngine().newProcessCase( getProcessDeployment().getId(),
                                            initialAttrs );
            }
            catch (NoSuchMessageException e)
            {
                // FIXME
                e.printStackTrace();
            }
            catch (NoSuchProcessException e)
            {
                // FIXME
                e.printStackTrace();
            }
        }
    }
}
