package com.werken.werkflow.engine;

import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.MessageInitiator;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.Arc;
import com.werken.werkflow.definition.petri.Place;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.definition.petri.ActivationRule;
import com.werken.werkflow.engine.rules.EnablingRule;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.Message;
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
    implements ProcessInfo
{
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private WorkflowEngine engine;

    private ProcessDefinition processDef;

    private Map rules;

    private Map messageTypes;

    private Correlator correlator;

    private Initiator initiator;
    
    public ProcessDeployment(WorkflowEngine engine,
                             ProcessDefinition processDef)
        throws ProcessDeploymentException
    {
        this.engine       = engine;
        this.processDef   = processDef;

        this.correlator   = new Correlator( engine,
                                            this );

        this.initiator    = new Initiator( engine,
                                           this );

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
        MessageInitiator[] msgInitiators = getProcessDefinition().getMessageInitiators();

        getInitiator().addMessageInitiators( msgInitiators );

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

        getCorrelator().addMessageWaiter( transition.getId(),
                                          messageWaiter );
    }

    private Correlator getCorrelator()
    {
        return this.correlator;
    }

    private Initiator getInitiator()
    {
        return this.initiator;
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
                    if ( getCorrelator().isCorrelated( processCase.getId(),
                                                       eachTrans ) )
                    {
                        enabledTrans.add( eachTrans );
                    }
                    else
                    {
                        msgWaitingTrans.add( eachTrans.getId() );
                    }
                }
            }
        }
        
        processCase.setEnabledTransitions( (Transition[]) enabledTrans.toArray( Transition.EMPTY_ARRAY ) );

        getCorrelator().evaluateCase( processCase,
                                      (String[]) msgWaitingTrans.toArray( EMPTY_STRING_ARRAY ) );
    }

    Object consumeMessage(String processCaseId,
                          Transition transition)
        throws NoSuchCorrelationException
    {
        return getCorrelator().consumeMessage( processCaseId,
                                               transition );
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