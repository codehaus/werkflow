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
    implements CaseEvaluator
{
    private ProcessDefinition processDefinition;

    Evaluator(ProcessDefinition processDefinition)
    {
        this.processDefinition  = processDefinition;
    }

    public CoreWorkItem[] evaluate(CoreProcessCase processCase)
    {
        synchronized ( processCase )
        {
            return (CoreWorkItem[]) evaluate( processCase,
                                              getPotentialTransitions( processCase ) ).toArray( CoreWorkItem.EMPTY_ARRAY );
        }
    }

    private List evaluate(CoreProcessCase processCase,
                          Transition[] potentials)
    {
        List workItems = new ArrayList();

        for ( int i = 0 ; i < potentials.length ; ++i )
        {
            workItems.addAll( evaluate( processCase,
                                        potentials[i] ) );
        }

        return workItems;
    }
    
    private List evaluate(CoreProcessCase processCase,
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
                                  processCase,
                                  transition,
                                  tokens );
        }
        else
        {
            if ( waiter instanceof MessageWaiter )
            {
                addMessageWaiterWorkItems( workItems,
                                           processCase,
                                           transition,
                                           tokens );
            }
        }

        return workItems;
    }

    private void addNoWaiterWorkItems(List workItems,
                                      CoreProcessCase processCase,
                                      Transition transition,
                                      String[] tokens)
    {
        workItems.add( new CoreWorkItem( processCase,
                                         transition,
                                         tokens ) );
    }

    private void addMessageWaiterWorkItems(List workItems,
                                           CoreProcessCase processCase,
                                           Transition transition,
                                           String[] tokens)
    {
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
                
                for ( int j = 0 ; j < tokens.length ; ++j )
                {
                    Transition trans = arcs[i].getTransition();
                    
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

    private ProcessDefinition getProcessDefinition()
    {
        return this.processDefinition;
    }

    private Place getPlace(String placeId)
        throws NoSuchPlaceException
    {
        return getProcessDefinition().getNet().getPlace( placeId );
    }
}
