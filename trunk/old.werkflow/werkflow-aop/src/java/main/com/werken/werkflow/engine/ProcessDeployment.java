package com.werken.werkflow.engine;

/*
 $Id$

 Copyright 2003 (C) The Werken Company. All Rights Reserved.
 
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
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.
 
 4. Products derived from this Software may not be called "werkflow"
    nor may "werkflow" appear in their names without prior written
    permission of The Werken Company. "werkflow" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://werkflow.werken.com/).
 
 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.
 
 */

import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.definition.MessageInitiator;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.Arc;
import com.werken.werkflow.definition.petri.Place;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.messaging.IncompatibleMessageSelectorException;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/** Actively deployed <code>ProcessDefintiion</code>.
 *
 *  @see WorkflowEngine
 *  @see ProcessDefinition
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class ProcessDeployment
    implements ProcessInfo
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    /** Empty <code>String</code> array. */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Workflow engine. */
    private WorkflowEngine engine;

    /** Deployed process-definition. */
    private ProcessDefinition processDef;

    /** Enabling rules. */
    private Map rules;

    /** Message correlator. */
    private Correlator correlator;

    /** Process initiator. */
    private Initiator initiator;
    
    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param engine The workflow engine.
     *  @param processDef The process-definition to deploy.
     *
     *  @throws ProcessDeploymentException If an error occurs while attempting
     *          to deploy the process definition.
     */
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

        initializeEnablingRules();
        initializeMessagingRules();
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Retrieve the <code>WorkflowEngine</code>.
     *
     *  @return The engine.
     */
    private WorkflowEngine getEngine()
    {
        return this.engine;
    }

    /** Initiatlize enabling rules.
     */
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

    /** Build an <code>EnablingRule</code> for a <code>Transition</code>.
     *
     *  @param transition The transition.
     */
    private void buildEnablingRule(Transition transition)
    {
        this.rules.put( transition,
                        new EnablingRule( transition ) );
    }

    /** Initialize all messaging rules.
     *
     *  @throws ProcessDeploymentException If an error occurs while
     *          attempting to initialize the messaging rules.
     */
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

    /** Build a messaging rule for a <code>Transition</code>.
     *
     *  @param transition The transition.
     *
     *  @throws IncompatibleMessageSelectorException If the transition
     *          uses a message-type with a message-selector incompatible
     *          with the messaging-manager.
     */
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

    /** Retrieve the <code>Correlator</code>.
     *
     *  @return The correlator.
     */
    private Correlator getCorrelator()
    {
        return this.correlator;
    }

    /** Retrieve the <code>Initiator</code>.
     *
     *  @return The initiator.
     */
    private Initiator getInitiator()
    {
        return this.initiator;
    }

    /** Retrieve the <code>ProcessDefinition</code>.
     *
     *  @return The process definition.
     */
    public ProcessDefinition getProcessDefinition()
    {
        return this.processDef;
    }

    /** Evaluate a <code>WorkflowProcessCase</code> for progress.
     *
     *  @param processCase The case to evaluate.
     */
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
                if ( eachTrans.getExpression() != null )
                {
                    try
                    {
                        if ( ! eachTrans.getExpression().evaluate( processCase ) )
                        {
                            continue;
                        }
                    }
                    catch (Exception e)
                    {
                        // FIXME
                        e.printStackTrace();
                    }
                }

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

    /** Consume an activating correlating message.
     *
     *  @param processCaseId The case id.
     *  @param transition The transition.
     *
     *  @return The consumed message.
     *
     *  @throws NoSuchCorrelationException If no correlation exists.
     */
    Object consumeMessage(String processCaseId,
                          Transition transition)
        throws NoSuchCorrelationException
    {
        return getCorrelator().consumeMessage( processCaseId,
                                               transition );
    }

    /** Retrieve potentially activatable <code>Transition</code>s.
     *
     *  @param processCase The process-case context.
     *
     *  @return The set of potentially activatable transitions.
     */
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
