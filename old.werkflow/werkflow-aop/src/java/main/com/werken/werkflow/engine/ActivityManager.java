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

import com.werken.werkflow.Attributes;
import com.werken.werkflow.NoSuchCaseException;
import com.werken.werkflow.NoSuchProcessException;
import com.werken.werkflow.ProcessException;
import com.werken.werkflow.action.Action;
import com.werken.werkflow.action.CallAction;
import com.werken.werkflow.activity.Activity;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.Waiter;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.petri.Place;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.definition.petri.Arc;
import com.werken.werkflow.definition.petri.NoSuchTransitionException;
import com.werken.werkflow.definition.petri.NoSuchPlaceException;
import com.werken.werkflow.expr.Expression;
import com.werken.werkflow.expr.ExpressionContext;
import com.werken.werkflow.expr.AttributesExpressionContext;
import com.werken.werkflow.task.Task;
import com.werken.werkflow.work.WorkItem;

import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Arrays;

/** Manager of <code>Activity</code> execution.
 *
 *  @see Action
 *  @see Activity
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
class ActivityManager
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    /** Empty <code>String</code> array. */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Workflow engine.*/
    private WorkflowEngine engine;

    /** In-flight activities. */
    private Set activities;

    /** In-flight call activities, indexed by child case id. */
    private Map callActivities;

    /** Thread pool. */
    private PooledExecutor pool;

    /** Queue feeding the thread pool. */
    private LinkedQueue queue;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param engine The workflow engine.
     */
    public ActivityManager(WorkflowEngine engine)
    {
        this.engine         = engine;
        this.activities     = new HashSet();
        this.callActivities = new HashMap();

        this.queue = new LinkedQueue();

        this.pool = new PooledExecutor( this.queue );
        this.pool.setMinimumPoolSize( 10 );
        this.pool.setKeepAliveTime( -1 );
        this.pool.waitWhenBlocked();
        this.pool.createThreads( 10 );
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

    /** Schedule a case for execution.
     *
     *  @param processCase The case to schedule.
     */
    public void scheduleCase(final WorkflowProcessCase processCase)
    {
        Transition[] transitions = processCase.getEnabledTransitions();

        boolean schedulable = false;

      TASKS_LOOP:
        for ( int i = 0 ; i < transitions.length ; ++i )
        {
            Task task = transitions[i].getTask();

            schedulable = true;
            break;
        } 

        if ( ! schedulable )
        {
            return;
        }

        try
        {
            this.queue.put( new Runnable()
                {
                    public void run()
                    {
                        synchronized ( processCase )
                        {
                            Transition[] enabledTrans = processCase.getEnabledTransitions();
                            
                            if ( enabledTrans.length == 0 )
                            {
                                return;
                            }
                            
                            Transition nextTrans = enabledTrans[0];

                            // System.err.println( "next trans: " + nextTrans.getId() );

                            fire( processCase,
                                  nextTrans );
                        }
                    }
                }
                            
                            );
        }
        catch (InterruptedException e)
        {
            // swallow
        }
    }

    /** Verify that a case's transition may execute.
     *
     *  @param processCase The case to verify.
     *  @param transition The candidate transition.
     *
     *  @return The map of additional execution attributes.
     *
     *  @throws VerificationException If the case's transition
     *          is not verified to be acceptable for execution.
     */
    protected Map verify(WorkflowProcessCase processCase,
                         Transition transition)
        throws VerificationException
    {
        // System.err.println( "verify: " + transition.getId() );

        Map otherAttrs = new HashMap();

        Transition[] enabledTrans = processCase.getEnabledTransitions();

        for ( int i = 0 ; i < enabledTrans.length ; ++i )
        {
            if ( enabledTrans[i] == transition )
            {
                Waiter waiter = transition.getWaiter();
                
                // System.err.println( " waiter: " + waiter );

                if ( waiter != null )
                {
                    if ( waiter instanceof MessageWaiter )
                    {
                        MessageWaiter msgWaiter = (MessageWaiter) waiter;

                        boolean shouldConsume = true;

                        try
                        {
                            ProcessDeployment deployment = getEngine().getProcessDeployment( processCase.getProcessInfo().getPackageId(),
                                                                                             processCase.getProcessInfo().getId() );
                            
                            ProcessDefinition processDef = deployment.getProcessDefinition(); 
                            
                            
                            if ( processDef.getInitiationType() == ProcessDefinition.InitiationType.MESSAGE )
                            {
                                Place in = processDef.getNet().getPlace( "in" );
                                
                                Arc[] inboundArcs = transition.getArcsFromPlaces();
                                
                                for ( int j = 0 ; j < inboundArcs.length ; ++j )
                                {
                                    if ( inboundArcs[j].getPlace().equals( in ) )
                                    {
                                        shouldConsume = false;
                                        break;
                                    }
                                }
                            }
                        }
                        catch (NoSuchProcessException e)
                        {
                            throw new VerificationException( processCase,
                                                             e );
                        }
                        catch (NoSuchPlaceException e)
                        {
                            throw new VerificationException( processCase,
                                                             e );
                        }

                        if ( shouldConsume )
                        {
                            try
                            {
                                Object message = getEngine().consumeMessage( processCase,
                                                                             transition );

                                // System.err.println( "consuming: " + message + " for " + transition.getId() + " bound to " + msgWaiter.getBindingVar() );

                                otherAttrs.put( msgWaiter.getBindingVar(),
                                                message );
                            }
                            catch (NoSuchCorrelationException e)
                            {
                                // e.printStackTrace();
                                throw new VerificationException( processCase );
                            }
                            catch (NoSuchProcessException e)
                            {
                                e.printStackTrace();
                                throw new VerificationException( processCase,
                                                                 e );
                            }
                        }
                    }
                }
            }
        }

        addStandardOtherAttributes( otherAttrs,
                                    processCase,
                                    transition );

        return otherAttrs;
    }

    void addStandardOtherAttributes(Map otherAttrs,
                                    WorkflowProcessCase processCase,
                                    Transition transition)
    {

        otherAttrs.put( "packageId",
                        processCase.getProcessInfo().getPackageId() );

        otherAttrs.put( "processId",
                        processCase.getProcessInfo().getId() );

        otherAttrs.put( "caseId",
                        processCase.getId() );

        otherAttrs.put( "transitionId",
                        transition.getId() );

    }

    /** Satisfy the execution of a case's transition by
     *  consuming marks.
     *
     *  @param processCase The case.
     *  @param transition The transition.
     *
     *  @return The identifiers of places from which marks were consumed.
     */
    protected String[] satisfy(WorkflowProcessCase processCase,
                               Transition transition)
    {
        return transition.getActivationRule().satisfy( transition,
                                                       processCase );
    }

    /** Fire the execution of a case's transition.
     *
     *  @param processCase The case.
     *  @param transition The transition.
     *
     *  @todo throw an exception
     *
     *  @return The activity handle or <code>null</code> if unsuccessful.
     */
    protected Activity fire(WorkflowProcessCase processCase,
                            Transition transition)
    {
        synchronized ( processCase )
        {
            try
            {
                Map otherAttrs = verify( processCase,
                                         transition);
                
                getEngine().notifyTransitionInitiated( processCase.getProcessInfo().getId(),
                                                       processCase.getId(),
                                                       transition.getId() );

                String[] placeIds = satisfy( processCase,
                                             transition );

                getEngine().notifyTokensConsumed( processCase.getProcessInfo().getId(),
                                                  processCase.getId(),
                                                  transition.getId(),
                                                  placeIds );

                
                return fire( processCase,
                             transition,
                             placeIds,
                             otherAttrs );
            }
            catch (VerificationException e)
            {
                // swallow
            }
        }

        return null;
    }

    protected Activity fireMessageInitiationActivity(WorkflowProcessCase processCase,
                                                     Transition transition,
                                                     Map otherAttrs)
    {

        synchronized ( processCase )
        {
            getEngine().notifyTransitionInitiated( processCase.getProcessInfo().getId(),
                                                   processCase.getId(),
                                                   transition.getId() );

            addStandardOtherAttributes( otherAttrs,
                                        processCase,
                                        transition );
            
            String[] placeIds = EMPTY_STRING_ARRAY;
            
            return fire( processCase,
                         transition,
                         placeIds,
                         otherAttrs );
        }
    }

    /** Fire a <code>WorkItem</code>.
     *
     *  @param workItem The work-item to fire.
     *
     *  @return The activity handle.
     *
     *  @throws NoSuchProcessException If the case's process is not current deployed.
     *  @throws NoSuchCaseException If the case cannot be located.
     */
    protected Activity fire(WorkItem workItem)
        throws NoSuchProcessException, NoSuchCaseException
    {
        WorkflowProcessCase processCase = getEngine().getProcessCase( workItem.getCaseId() );

        Transition[] enabledTrans = processCase.getEnabledTransitions();
        
        String transId = workItem.getTransitionId();
        
        for ( int i = 0 ; i < enabledTrans.length ; ++i )
        {
            if ( enabledTrans[i].getId().equals( transId ) )
            {
                return fire( processCase,
                             enabledTrans[i] );
            }
        }
        
        return null;
    }

    /** Fire an activity.
     *
     *  @param processCase The process case.
     *  @param transition The transition.
     *  @param placeIds The identifiers of the places from which marks were consumed.
     *  @param otherAttrs Additional context attributes.
     *
     *  @return The activity handle.
     */
    protected Activity fire(WorkflowProcessCase processCase,
                            Transition transition,
                            String[] placeIds,
                            Map otherAttrs)
    {
        Task task = transition.getTask();

        Map caseAttrs = processCase.getCaseAttributes();

        boolean isCall = false;

        if ( task != null )
        {
            isCall = ( task.getAction() instanceof CallAction );
        }

        WorkflowActivity activity = newActivity( processCase.getProcessInfo().getPackageId(),
                                                 processCase.getProcessInfo().getId(),
                                                 processCase.getId(),
                                                 transition.getId(),
                                                 placeIds,
                                                 caseAttrs,
                                                 isCall );

        if ( task == null )
        {
            complete( activity );
        }
        else
        {
            fire( activity,
                  processCase,
                  task,
                  otherAttrs );
        }
            
        try
        {
            getEngine().evaluateCase( processCase );
        }
        catch (NoSuchProcessException e)
        {
            // FIXME
            e.printStackTrace();
        }

        return activity;
    }

    /** Perform an activity.
     *
     *  @param activity The activity to perform.
     *  @param processCase The processCase.
     *  @param task The task.
     *  @param otherAttrs Additional context attributes.
     */
    protected void fire(WorkflowActivity activity,
                        WorkflowProcessCase processCase,
                        Task task,
                        Map otherAttrs)
    {
        if ( activity.isCall() )
        {
            CallAction action = (CallAction) task.getAction();
            
            String packageId = action.getPackageId();
            String processId = action.getProcessId();

            try
            {
                Attributes attrs = action.getAttributes( processCase );

                try
                {
                    getEngine().callChildProcess( activity,
                                                  packageId,
                                                  processId,
                                                  attrs );
                }
                catch (NoSuchProcessException e)
                {
                    e.printStackTrace();
                }
                catch (ProcessException e)
                {
                    e.printStackTrace();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Action action = task.getAction();
            
            action.perform( activity,
                            activity.getCaseAttributes(),
                            otherAttrs );
        }
    }

    void newChildProcessCase(WorkflowActivity activity,
                             WorkflowProcessCase childCase)
    {
        this.callActivities.put( childCase.getId(),
                                 activity );
    }

    /** Receive completion notification.
     *
     *  @param activity The completed activity.
     */
    protected void complete(WorkflowActivity activity)
    {
        try
        {
            WorkflowProcessCase processCase = getEngine().getProcessCase( activity.getCaseId() );

            synchronized ( processCase )
            {
                processCase.mergeCaseAttributes( activity.getCaseAttributes() );

                ProcessDeployment deployment = getEngine().getProcessDeployment( activity.getPackageId(),
                                                                                 activity.getProcessId() );
                ProcessDefinition processDef = deployment.getProcessDefinition();

                Transition transition = processDef.getNet().getTransition( activity.getTransitionId() );

                String[] placeIds = produceTokens( processCase,
                                                   processDef,
                                                   transition );

                getEngine().notifyTokensProduced( activity.getProcessId(),
                                                  activity.getCaseId(),
                                                  activity.getTransitionId(),
                                                  placeIds );
                
                this.activities.remove( activity );

                getEngine().notifyTransitionTerminated( activity.getProcessId(),
                                                        activity.getCaseId(),
                                                        activity.getTransitionId() );

                processCase.getState().store();
            }

            getEngine().evaluateCase( processCase );
        }
        catch (NoSuchTransitionException e)
        {
            // FIXME
            e.printStackTrace();
        }
        catch (NoSuchProcessException e)
        {
            // FIXME
            e.printStackTrace();
        }
        catch (NoSuchCaseException e)
        {
            // FIXME
            e.printStackTrace();
        }
    }

    void processCaseComplete(String caseId)
    {
        WorkflowActivity activity = (WorkflowActivity) this.callActivities.get( caseId );

        if ( activity != null )
        {
            complete( activity );
        }
    }

    String[] produceTokens(WorkflowProcessCase processCase,
                           ProcessDefinition processDef,
                           Transition transition)
    {
        List placeIds = new ArrayList();
        
        Arc[] arcs = transition.getArcsToPlaces();

        // System.err.println( "produce tokens for : " + transition.getId() );
        
        ExpressionContext exprContext = new AttributesExpressionContext( processCase );

        for ( int i = 0 ; i < arcs.length ; ++i )
        {
            Expression expr = arcs[i].getExpression();
            
            if ( expr != null )
            {
                try
                {
                    if ( ! expr.evaluateAsBoolean( exprContext ) )
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
            
            Place place = arcs[i].getPlace();

            // System.err.println( "   produce: " + place.getId() + " // " + place );
            
            processCase.addMark( place.getId() );
            
            placeIds.add( place.getId() );
        }

        return (String[]) placeIds.toArray( EMPTY_STRING_ARRAY );

    }

    /** Receive completion-with-error notification.
     *
     *  @param activity The completed activity.
     *  @param error The error
     */
    protected void completeWithError(WorkflowActivity activity,
                                     Throwable error)
    {
        try
        {
            error.printStackTrace();

            WorkflowProcessCase processCase = getEngine().getProcessCase( activity.getCaseId() );

            synchronized( processCase )
            {
                
                String[] placeIds = activity.getPlaceIds();
                
                for ( int i = 0 ; i < placeIds.length ; ++i )
                {
                    processCase.addMark( placeIds[i] );
                }
                
                getEngine().evaluateCase( processCase );

                getEngine().notifyTokensRolledBack( activity.getProcessId(),
                                                    activity.getCaseId(),
                                                    activity.getTransitionId(),
                                                    activity.getPlaceIds() );
                

                this.activities.remove( activity );

                getEngine().notifyTransitionTerminated( activity.getProcessId(),
                                                        activity.getCaseId(),
                                                        activity.getTransitionId(),
                                                        error );

                processCase.getState().store();
            }

            getEngine().evaluateCase( processCase );
        }
        catch (NoSuchCaseException e)
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

    /** Create a new activity handle.
     *
     *  @param processId The package identifier.
     *  @param processId The process identifier.
     *  @param caseId The case identifier.
     *  @param transitionId The transition identifier.
     *  @param placeIds The identifiers of the places from which marks were consumed.
     *  @param caseAttrs Case attributes.
     *
     *  @return The new activity handle.
     */
    protected WorkflowActivity newActivity(String packageId,
                                           String processId,
                                           String caseId,
                                           String transitionId,
                                           String[] placeIds,
                                           Map caseAttrs,
                                           boolean isCall)
    {
        WorkflowActivity activity = new WorkflowActivity( this,
                                                          packageId,
                                                          processId,
                                                          caseId,
                                                          transitionId,
                                                          placeIds,
                                                          caseAttrs,
                                                          isCall );

        if ( ! isCall )
        {
            this.activities.add( activity );
        }

        return activity;
    }

    /** Retrieve an activity.
     *
     *  @param caseId The case identifier.
     *  @param transitionId The transition identifier.
     *
     *  @return The activity handle.
     */
    protected WorkflowActivity getActivity(String caseId,
                                           String transitionId)
    {
        Iterator activityIter = this.activities.iterator();
        WorkflowActivity eachActivity = null;

        while ( activityIter.hasNext() )
        {
            eachActivity = (WorkflowActivity) activityIter.next();

            if ( eachActivity.getCaseId().equals( caseId )
                 &&
                 eachActivity.getTransitionId().equals( transitionId ) )
            {
                return eachActivity;
            }
        }

        return null;
    }

    /** Retrieve the in-progress activities for a case.
     *
     *  @param caseId The case identifier.
     *
     *  @return The possibly empty array of in-progress activities.
     */
    Activity[] getActivitiesForProcessCase(String caseId)
    {
        List caseActivities = new ArrayList();

        Iterator activityIter = this.activities.iterator();
        WorkflowActivity eachActivity = null;

        while ( activityIter.hasNext() )
        {
            eachActivity = (WorkflowActivity) activityIter.next();

            if ( eachActivity.getCaseId().equals( caseId ) )
            {
                caseActivities.add( eachActivity );
            }
        }

        return (Activity[]) caseActivities.toArray( Activity.EMPTY_ARRAY );
    }
}
