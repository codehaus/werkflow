package com.werken.werkflow.core;

import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.Waiter;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.petri.Arc;
import com.werken.werkflow.definition.petri.Place;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.definition.petri.ActivationRule;
import com.werken.werkflow.definition.petri.AndInputRule;
import com.werken.werkflow.definition.petri.NoSuchPlaceException;

import java.util.List;
import java.util.ArrayList;

class Evaluator
//    implements CaseEvaluator
{
    private ProcessDeployment processDeployment;

    Evaluator(ProcessDeployment processDeployment)
    {
        this.processDeployment  = processDeployment;
    }

    public CoreWorkItem[] evaluate(CoreChangeSet changeSet,
                                   CoreProcessCase processCase)
    {
        synchronized ( processCase )
        {
            return (CoreWorkItem[]) evaluate( changeSet,
                                              processCase,
                                              getPotentialTransitions( processCase ) ).toArray( CoreWorkItem.EMPTY_ARRAY );
        }
    }

    private List evaluate(CoreChangeSet changeSet,
                          CoreProcessCase processCase,
                          Transition[] potentials)
    {
        List workItems = new ArrayList();

        for ( int i = 0 ; i < potentials.length ; ++i )
        {
            workItems.addAll( evaluate( changeSet,
                                        processCase,
                                        potentials[i] ) );
        }

        return workItems;
    }
    
    private List evaluate(CoreChangeSet changeSet,
                          CoreProcessCase processCase,
                          Transition transition)
    {
        ActivationRule rule = transition.getActivationRule();

        if ( rule == null )
        {
            rule = AndInputRule.getInstance();
        }

        String[] tokens = null;

        try
        {
            tokens = rule.getSatisfyingTokens( transition,
                                               processCase,
                                               processCase.getTokens() );
        }
        catch (Exception e)
        {
            // INTERNAL ERROR
            e.printStackTrace();
            return null;
        }

        if ( tokens.length == 0 )
        {
            return null;
        }

        Waiter waiter = transition.getWaiter();

        List workItems = new ArrayList();

        if ( waiter == null )
        {
            addNoWaiterWorkItems( workItems,
                                  changeSet,
                                  processCase,
                                  transition,
                                  tokens );
        }
        else
        {
            if ( waiter instanceof MessageWaiter )
            {
                addMessageWaiterWorkItems( workItems,
                                           changeSet,
                                           processCase,
                                           transition,
                                           tokens );
            }
        }

        return workItems;
    }

    private void addNoWaiterWorkItems(List workItems,
                                      CoreChangeSet changeSet,
                                      CoreProcessCase processCase,
                                      Transition transition,
                                      String[] tokens)
    {
        workItems.add( new CoreWorkItem( processCase,
                                         transition,
                                         tokens ) );
    }

    private void addMessageWaiterWorkItems(List workItems,
                                           CoreChangeSet changeSet,
                                           CoreProcessCase processCase,
                                           Transition transition,
                                           String[] tokens)
    {
        /*
        getProcessDeployment().addCase( processCase,
                                        transition.getId() );
        */

        Correlation[] correlations = processCase.getCorrelations( transition.getId() );
        
        for ( int i = 0 ; i < correlations.length ; ++i )
        {
            workItems.add( new CoreWorkItem( processCase,
                                             transition,
                                             tokens,
                                             correlations[i].getMessageId() ) );
        }
    }

    private Transition[] getPotentialTransitions(CoreProcessCase processCase)
    {
        List potentials = new ArrayList();

        String[] tokens = processCase.getTokens();

        for ( int i = 0 ; i < tokens.length ; ++i )
        {
            try
            {
                Place place = getPlace( tokens[i] );
                
                Arc[] arcs = place.getArcsToTransitions();
                
                for ( int j = 0 ; j < arcs.length ; ++j )
                {
                    Transition trans = arcs[j].getTransition();
                    
                    if ( ! potentials.contains( trans ) )
                    {
                        potentials.add( trans );
                    }
                }
            }
            catch (NoSuchPlaceException e)
            {
                // INTERNAL ERROR
                e.printStackTrace();
            }
        }

        return (Transition[]) potentials.toArray( Transition.EMPTY_ARRAY );
    }

    private ProcessDeployment getProcessDeployment()
    {
        return this.processDeployment;
    }

    private Place getPlace(String placeId)
        throws NoSuchPlaceException
    {
        return getProcessDeployment().getProcessDefinition().getNet().getPlace( placeId );
    }
}
