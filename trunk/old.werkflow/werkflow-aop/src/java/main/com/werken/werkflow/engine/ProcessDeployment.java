package com.werken.werkflow.engine;

import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.Arc;
import com.werken.werkflow.definition.petri.Place;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.definition.petri.ActivationRule;
import com.werken.werkflow.engine.rules.EnablingRule;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.MessageSink;
import com.werken.werkflow.service.messaging.IncompatibleMessageSelectorException;
import com.werken.werkflow.task.Task;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ProcessDeployment
    implements ProcessInfo, MessageSink
{
    private WorkflowEngine engine;

    private ProcessDefinition processDef;

    private Map rules;
    private Map messageTypes;
    private Map correlators;

    public ProcessDeployment(WorkflowEngine engine,
                             ProcessDefinition processDef)
        throws ProcessDeploymentException
    {
        this.engine       = engine;
        this.processDef   = processDef;
        this.correlators = new HashMap();

        this.messageTypes = new HashMap();

        MessageType[] types = processDef.getMessageTypes();

        for ( int i = 0 ; i < types.length ; ++i )
        {
            this.messageTypes.put( types[i].getId(),
                                   types[i] );
        }

        initializeEnablingRules();
        initializeMessagingRules();
    }

    public WorkflowEngine getEngine()
    {
        return this.engine;
    }

    private void initializeEnablingRules()
    {
        this.rules = new HashMap();

        Net net = getProcessDefinition().getNet();

        Transition[] transitions = net.getTransitions();

        for ( int i = 0 ; i < transitions.length ; ++i )
        {
            buildEnablingRule( transitions[i] );
        }
    }

    private void buildEnablingRule(Transition transition)
    {
        this.rules.put( transition,
                        new EnablingRule( transition ) );
    }

    private void initializeMessagingRules()
        throws ProcessDeploymentException
    {
        
        try
        {
            Net net = getProcessDefinition().getNet();
            
            Transition[] transitions = net.getTransitions();
            
            for ( int i = 0 ; i < transitions.length ; ++i )
            {
                buildMessagingRule( transitions[i] );
            }
        }
        catch (IncompatibleMessageSelectorException e)
        {
            throw new ProcessDeploymentException( e );
        }
    }

    private void buildMessagingRule(Transition transition)
        throws IncompatibleMessageSelectorException
    {
        MessageWaiter messageWaiter = transition.getMessageWaiter();

        if ( messageWaiter == null )
        {
            return;
        }

        MessageType messageType = getMessageType( messageWaiter.getMessageTypeId() );

        Correlator correlator = (Correlator) this.correlators.get( messageType );

        if ( correlator == null )
        {
            correlator = new Correlator( getEngine(),
                                         messageType );

            this.correlators.put( messageType,
                                  correlator );
        }

        correlator.addMessageWaiter( transition.getId(),
                                     messageWaiter );
    }

    public void acceptMessage(MessageType messageType,
                              Object message)
    {
        Correlator correlator = (Correlator) this.correlators.get( messageType );

        correlator.addMessage( message );
    }

    protected MessageType getMessageType(String id)
    {
        return (MessageType) this.messageTypes.get( id );
    }

    public ProcessDefinition getProcessDefinition()
    {
        return this.processDef;
    }

    void evaluateCase(WorkflowProcessCase processCase)
    {
        Set potentialTrans  = getPotentialTransitions( processCase );
        Set enabledTrans    = new HashSet();
        Set msgWaitingTrans = new HashSet();
        
        Iterator   transIter = potentialTrans.iterator();
        Transition eachTrans = null;
        EnablingRule rule = null;

        while ( transIter.hasNext() )
        {
            eachTrans = (Transition) transIter.next();

            rule = (EnablingRule) this.rules.get( eachTrans );

            if ( rule.evaluate( processCase ) )
            {
                if ( eachTrans.getMessageWaiter() == null )
                {
                    enabledTrans.add( eachTrans );
                }
                else
                {
                    msgWaitingTrans.add( eachTrans );
                }
            }
        }

        processCase.setEnabledTransitions( (Transition[]) enabledTrans.toArray( Transition.EMPTY_ARRAY ) );

        Iterator   waitingTransIter = msgWaitingTrans.iterator();
        Transition eachWaitingTrans = null;

        while ( waitingTransIter.hasNext() )
        {
            eachWaitingTrans = (Transition) waitingTransIter.next();

            MessageWaiter waiter = eachWaitingTrans.getMessageWaiter();

            if ( waiter != null )
            {
                MessageType messageType = getMessageType( waiter.getMessageTypeId() );

                Correlator correlator = (Correlator) this.correlators.get( messageType );

                correlator.addBlocker( eachWaitingTrans.getId(),
                                       processCase );
            }
        }
    }

    Set getPotentialTransitions(WorkflowProcessCase processCase)
    {
        String[] marks = processCase.getMarks();

        Set transitions = new HashSet();

        Net   net   = getProcessDefinition().getNet();
        Arc[] arcs  = null;

        Place[] places = net.getPlaces();

        for ( int i = 0 ; i < places.length ; ++i )
        {
            if ( processCase.hasMark( places[i].getId() ) )
            {
                arcs = places[i].getArcsToTransitions();
                
                for ( int j = 0 ; j < arcs.length ; ++j )
                {
                    transitions.add( arcs[j].getTransition() );
                }
            }
        }

        return transitions;
    }

    /** @see ProcessInfo
     */
    public String getId()
    {
        return getProcessDefinition().getId();
    }

    /** @see ProcessInfo
     */
    public String getDocumentation()
    {
        return getProcessDefinition().getDocumentation();
    }
}
