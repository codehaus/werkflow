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
import com.werken.werkflow.engine.rules.CorrelatingRule;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.MessageSink;
import com.werken.werkflow.task.Task;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.DroolsException;
import org.drools.rule.RuleSet;

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
    private ProcessDefinition processDef;

    private Map rules;
    private List registrations;
    private Map messageTypes;

    private RuleSet messagingRuleSet;
    private RuleBase messagingRuleBase;
    private WorkingMemory messagingMemory;;

    public ProcessDeployment(ProcessDefinition processDef)
        throws ProcessDeploymentException
    {
        this.processDef = processDef;
        this.registrations = new ArrayList();
        this.messageTypes = new HashMap();

        MessageType[] messageTypes = processDef.getMessageTypes();

        for ( int i = 0 ; i < messageTypes.length ; ++i )
        {
            this.messageTypes.put( messageTypes[i].getId(),
                                   messageTypes[i] );
        }

        initializeRules();
    }

    private void initializeRules()
        throws ProcessDeploymentException
    {
        this.rules = new HashMap();

        this.messagingRuleSet = new RuleSet( "rules.messaging" );
        this.messagingRuleBase = new RuleBase();
        this.messagingMemory = this.messagingRuleBase.createWorkingMemory();

        Net net = getProcessDefinition().getNet();

        Transition[] transitions = net.getTransitions();

        for ( int i = 0 ; i < transitions.length ; ++i )
        {
            buildRules( transitions[i] );
        }

        try
        {
            this.messagingRuleBase.addRuleSet( this.messagingRuleSet );
        }
        catch (DroolsException e)
        {
            throw new ProcessDeploymentException( e );
        }
    }

    private void buildRules(Transition transition)
        throws ProcessDeploymentException
    {
        buildEnablingRule( transition );
        buildMessagingRules( transition );
    }

    private void buildEnablingRule(Transition transition)
        throws ProcessDeploymentException
    {
        this.rules.put( transition,
                        new EnablingRule( transition ) );
    }

    private void buildMessagingRules(Transition transition)
        throws ProcessDeploymentException
    {
        MessageWaiter waiter = transition.getMessageWaiter();

        if ( waiter == null )
        {
            return;
        }

        MessageType messageType = getMessageType( waiter.getMessageTypeId() );

        CorrelatingRule rule = new CorrelatingRule( transition,
                                                    messageType );

        try
        {
            this.messagingRuleSet.addRule( rule );
        }
        catch (DroolsException e)
        {
            throw new ProcessDeploymentException( e );
        }
    }

    public void acceptMessage(MessageType messageType,
                              Object message)
    {
        /*
        getMessagingRules().assertObject( new Message( messageType,
                                                       message ) );
        */
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
