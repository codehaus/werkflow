package org.codehaus.werkflow.core;

/*
 $Id$

 Copyright 2003 (C) The Codehaus. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "werkflow" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Codehaus.  For written permission,
    please contact info@codehaus.org.

 4. Products derived from this Software may not be called "werkflow"
    nor may "werkflow" appear in their names without prior written
    permission of The Codehaus. "werkflow" is a registered
    trademark of The Codehaus.

 5. Due credit should be given to The Codehaus -
    http://werkflow.codehaus.org/

 THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import org.codehaus.werkflow.definition.ProcessDefinition;
import org.codehaus.werkflow.definition.Waiter;
import org.codehaus.werkflow.definition.MessageWaiter;
import org.codehaus.werkflow.definition.petri.Arc;
import org.codehaus.werkflow.definition.petri.Place;
import org.codehaus.werkflow.definition.petri.Transition;
import org.codehaus.werkflow.definition.petri.ActivationRule;
import org.codehaus.werkflow.definition.petri.AndInputRule;
import org.codehaus.werkflow.definition.petri.NoSuchPlaceException;

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
