package com.werken.werkflow.engine;

import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.Arc;
import com.werken.werkflow.definition.petri.Place;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.definition.petri.ActivationRule;
import com.werken.werkflow.engine.rules.EnablingRule;
import com.werken.werkflow.task.Task;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class ProcessDeployment
    implements ProcessInfo
{
    private ProcessDefinition processDef;

    private Map rules;

    public ProcessDeployment(ProcessDefinition processDef)
    {
        this.processDef = processDef;

        initializeRules();
    }

    private void initializeRules()
    {
        this.rules = new HashMap();

        Net net = getProcessDefinition().getNet();

        Transition[] transitions = net.getTransitions();

        for ( int i = 0 ; i < transitions.length ; ++i )
        {
            buildRule( transitions[i] );
        }
    }

    private void buildRule(Transition transition)
    {
        this.rules.put( transition,
                        new EnablingRule( transition ) );
    }

    public ProcessDefinition getProcessDefinition()
    {
        return this.processDef;
    }

    void evaluateCase(WorkflowProcessCase processCase)
    {
        Set potentialTrans = getPotentialTransitions( processCase );
        Set enabledTrans   = new HashSet();
        
        Iterator   transIter = potentialTrans.iterator();
        Transition eachTrans = null;
        EnablingRule rule = null;

        while ( transIter.hasNext() )
        {
            eachTrans = (Transition) transIter.next();

            rule = (EnablingRule) this.rules.get( eachTrans );

            if ( rule.evaluate( processCase ) )
            {
                enabledTrans.add( eachTrans );
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
