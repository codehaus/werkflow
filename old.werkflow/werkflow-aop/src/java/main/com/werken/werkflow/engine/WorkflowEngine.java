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
import com.werken.werkflow.SimpleAttributes;
import com.werken.werkflow.Wfms;
import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.WfmsRuntime;
import com.werken.werkflow.NoSuchCaseException;
import com.werken.werkflow.NoSuchProcessException;
import com.werken.werkflow.QueryException;
import com.werken.werkflow.activity.Activity;
import com.werken.werkflow.admin.WfmsAdmin;
import com.werken.werkflow.admin.ProcessException;
import com.werken.werkflow.admin.DuplicateProcessException;
import com.werken.werkflow.admin.ProcessVerificationException;
import com.werken.werkflow.definition.Expression;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.ProcessPackage;
import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.Place;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.definition.petri.Arc;
import com.werken.werkflow.definition.petri.PetriException;
import com.werken.werkflow.event.WfmsEventListener;
import com.werken.werkflow.event.ProcessDeployedEvent;
import com.werken.werkflow.event.ProcessUndeployedEvent;
import com.werken.werkflow.event.CaseInitiatedEvent;
import com.werken.werkflow.event.CaseTerminatedEvent;
import com.werken.werkflow.event.TransitionInitiatedEvent;
import com.werken.werkflow.event.TransitionTerminatedEvent;
import com.werken.werkflow.event.TokensProducedEvent;
import com.werken.werkflow.event.TokensConsumedEvent;
import com.werken.werkflow.event.TokensRolledBackEvent;
import com.werken.werkflow.log.Log;
import com.werken.werkflow.log.SimpleLog;
import com.werken.werkflow.service.WfmsServices;
import com.werken.werkflow.service.caserepo.CaseState;
import com.werken.werkflow.service.messaging.MessageSink;
import com.werken.werkflow.service.messaging.Registration;
import com.werken.werkflow.service.messaging.IncompatibleMessageSelectorException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;

/** Core implementation of <code>Wfms</code>.
 *
 *  <p>
 *  An instance of <code>WorkflowEngine</code> may be instantiated
 *  either directly or via the <code>PlexusWfms</code> avalon-plexus
 *  component.
 *  </p>
 *
 *  <p>
 *  To assist the <code>WorkflowEngine</code>, a configured <code>WfmsServices</code>
 *  should be passed in during construction.  This allows the engine to access
 *  the case storage repository and messaging manager at runtime.
 *  </p>
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class WorkflowEngine
    implements Wfms
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Log sink. */
    private Log log;

    /** External services. */
    private WfmsServices services;

    /** Admin interface implementation. */
    private WfmsAdmin admin;

    /** Runtime interface implementation. */
    private WfmsRuntime runtime;

    /** Activity-execution manager. */
    private ActivityManager activityManager;

    /** Deployed processes. */
    private Map deployments;

    /** Currently paged-in cases. */
    private Map cases;

    /** Wfms event listeners. */
    private List listeners;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  <p>
     *  Create an engine that uses its own simple logging mechanism.
     *  </p>
     *
     *  @param services The runtime external services.
     */
    public WorkflowEngine(WfmsServices services)
    {
        this( services,
              new SimpleLog( "WorkflowEngine" ) );
    }

    /** Construct.
     *
     *  @param services The runtime external services.
     *  @param log The root log sink.
     */
    public WorkflowEngine(WfmsServices services,
                          Log log)
    {
        this.log      = log;
        this.services = services;

        this.activityManager = new ActivityManager( this );

        this.admin   = new WorkflowAdmin( this );
        this.runtime = new WorkflowRuntime( this );

        this.deployments = new HashMap();
        this.cases       = new HashMap();

        this.listeners   = new ArrayList();
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Retrieve the log sink.
     *
     *  @return The log sink.
     */
    Log getLog()
    {
        return this.log;
    }

    /** Retrieve the external services broker.
     *
     *  @return The external services broker.
     */
    WfmsServices getServices()
    {
        return this.services;
    }

    /** @see Wfms
     */
    public WfmsRuntime getRuntime()
    {
        return this.runtime;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     com.werken.werkflow.WfmsRuntime
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the <code>ProcessInfo</code> descriptors for
     *  all available deployed processes.
     *
     *  @return The process-info descriptors.
     */
    ProcessInfo[] getProcesses()
    {
        return (ProcessInfo[]) this.deployments.values().toArray( ProcessInfo.EMPTY_ARRAY );
    }

    /** Retrieve the <code>ProcessInfo</code> descriptor for
     *  a specific process.
     *
     *  @param processId The process id.
     *
     *  @return The process-info descriptor.
     *
     *  @throws NoSuchProcessException If the <code>processId</code>
     *          does not refer to a valid deployed process.
     */
    ProcessInfo getProcess(String processId)
        throws NoSuchProcessException
    {
        return getProcessDeployment( processId );
    }
    
    /** Create a new <code>ProcessCase</code> for a particular process.
     *
     *  @param processId The id of the process.
     *  @param attributes The initial attributes for the case.
     *
     *  @return The newly created process case.
     *
     *  @throws NoSuchProcessException If the process identifier does
     *          not refer to a currently deployed process definition.
     */
    WorkflowProcessCase newProcessCase(String processId,
                                       Attributes attributes)
        throws NoSuchProcessException
    {
        CaseState caseState = newCaseState( processId,
                                            attributes );

        notifyCaseInitiated( processId,
                             caseState.getCaseId() );

        return assumeCase( caseState );
    }

    WorkflowProcessCase newMessageInitiatedProcessCase(ProcessDeployment deployment,
                                                       Transition transition,
                                                       Map attributes,
                                                       Map otherAttrs)
    {
        CaseState caseState = newCaseState( deployment.getId(),
                                            new SimpleAttributes( attributes  ) );

        notifyCaseInitiated( deployment.getId(),
                             caseState.getCaseId() );

        WorkflowProcessCase processCase = new WorkflowProcessCase( deployment,
                                                                   caseState );

        this.cases.put( processCase.getId(),
                        processCase );

        processCase.removeMark( "in" );

        processCase.getState().store();

        getActivityManager().fireMessageInitiationActivity( processCase,
                                                            transition,
                                                            otherAttrs );

        return processCase;
    }

    /** Retrieve a <code>ProcessCase</code> by its id.
     *
     *  @param caseId The case id.
     *
     *  @return The case associated with the id.
     *
     *  @throws NoSuchCaseException If the identifier does not
     *          match any case known by the system.
     *  @throws NoSuchProcessException If the identifier do match
     *          a case known by the system, but the process associated
     *          with the case is not currently deployed.
     */
    WorkflowProcessCase getProcessCase(String caseId)
        throws NoSuchCaseException, NoSuchProcessException
    {
        if ( this.cases.containsKey( caseId ) )
        {
            return (WorkflowProcessCase) this.cases.get( caseId );
        }

        CaseState caseState = getCaseState( caseId );

        if ( caseState == null )
        {
            throw new NoSuchCaseException( caseId );
        }

        return assumeCase( caseState );
    }

    /** Select <code>ProcessCase</code>s by process-id and a place-id.
     *
     *  <p>
     *  All process-cases of the specified process containing a mark
     *  in the specified place are returned.
     *  </p>
     *
     *  @param processId The process identifier.
     *  @param placeId The place identifier.
     *
     *  @return The selected cases.
     *
     *  @throws QueryException If an error occurs while attempting to
     *          evaluate the selection query.
     */
    ProcessCase[] selectCases(String processId,
                              String placeId)
        throws QueryException
    {
        String[] caseIds = getServices().getCaseRepository().selectCases( processId,
                                                                          placeId );
        
        ProcessCase[] cases = new ProcessCase[ caseIds.length ];
        
        try
        {
            for ( int i = 0 ; i < caseIds.length ; ++i )
            {
                cases[i] = getProcessCase( caseIds[i] );
            }
        }
        catch (NoSuchCaseException e)
        {
            throw new QueryException( e );
        }
        catch (NoSuchProcessException e)
        {
            throw new QueryException( e );
        }
        
        return cases;
    }

    /** Select <code>ProcessCase</code>s by process-id and
     *  query-by-example case attributes.
     *
     *  @param processId The process identifier.
     *  @param qbeAttrs The query-by-example attributes.
     *
     *  @return The selected cases.
     *
     *  @throws QueryException If an error occurs while attempting to
     *          evaluate the selection query.
     */
    ProcessCase[] selectCases(String processId,
                              Map qbeAttrs)
        throws QueryException
    {
        String[] caseIds = getServices().getCaseRepository().selectCases( processId,
                                                                          qbeAttrs );
        
        ProcessCase[] cases = new ProcessCase[ caseIds.length ];
        
        try
        {
            for ( int i = 0 ; i < caseIds.length ; ++i )
            {
                cases[i] = getProcessCase( caseIds[i] );
            }
        }
        catch (NoSuchCaseException e)
        {
            throw new QueryException( e );
        }
        catch (NoSuchProcessException e)
        {
            throw new QueryException( e );
        }
            
        return cases;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     com.werken.werkflow.admin.WfmsAdmin
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** @see Wfms
     */
    public WfmsAdmin getAdmin()
    {
        return this.admin;
    }

    void deployProcessPackage(ProcessPackage pkg)
        throws ProcessException
    {
        ProcessDefinition[] defs = pkg.getProcessDefinitions();

        for ( int i = 0 ; i < defs.length ; ++i )
        {
            deployProcess( defs[i] );
        }
    }

    /** Deploy a <code>ProcessDefinition</code> making it available
     *  for case creation and manipulation.
     *
     *  @param processDef The process definition to deploy.
     *
     *  @throws ProcessException If an error occurs while attempting to
     *          deploy the process.
     */
    void deployProcess(ProcessDefinition processDef)
        throws ProcessException
    {
        String processId = processDef.getId();

        if ( this.deployments.containsKey( processId ) )
        {
            throw new DuplicateProcessException( processDef );
        }

        verifyProcess( processDef );

        deployVerifiedProcess( processDef );

        notifyProcessDeployed( processDef );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Assume management control of a <code>CaseState</code>.
     *
     *  @param caseState The case-state to assume control of.
     *
     *  @return The assumed process-case.
     *
     *  @throws NoSuchProcessException If the case-state is associated with
     *          a process not currently deployed within the engine.
     */
    WorkflowProcessCase assumeCase(CaseState caseState)
        throws NoSuchProcessException
    {
        ProcessDeployment deployment = getProcessDeployment( caseState.getProcessId() );

        WorkflowProcessCase processCase = new WorkflowProcessCase( deployment,
                                                                   caseState );

        this.cases.put( processCase.getId(),
                        processCase );

        evaluateCase( processCase );

        return processCase;
    }

    /** Perform pre-deployment verification of a <code>ProcessDefinition</code>.
     *
     *  @param processDef The process-definition to verify.
     *
     *  @throws ProcessVerificationException If the process definition does not
     *          pass the verification procedures.
     */
    void verifyProcess(ProcessDefinition processDef)
        throws ProcessVerificationException
    {
        //verifyNet( processDef );
    }

    /** Deploy an verified <code>ProcessDefinition</code>.
     *
     *  @param processDef The process-definition to deploy.
     *
     *  @throws ProcessException If an error occurs while attempting to
     *          deploy the process.
     */
    void deployVerifiedProcess(ProcessDefinition processDef)
        throws ProcessException
    {
        try
        {
            ProcessDeployment deployment = new ProcessDeployment( this,
                                                                  processDef );
            
            this.deployments.put( processDef.getId(),
                                  deployment );
        }
        catch (ProcessDeploymentException e)
        {
            throw new ProcessException( processDef,
                                        e );
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     com.werken.werkflow.service.messaging.MessagingManager
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Register a <code>MessageSink</code> as interested in messages
     *  of a particular <code>MessageType</code>.
     *
     *  @see com.werken.werkflow.service.messaging.MessagingManager
     *  @see com.werken.werkflow.service.messaging.Registration
     *
     *  @param messageSink The message sink.
     *  @param messageType The message-type of interest.
     *
     *  @return The new registration.
     *
     *  @throws IncompatibleMessageSelectorException If the message-type
     *          contains a message-selector incompatible with the underlying
     *          messaging-manager.
     */
    Registration register(MessageSink messageSink,
                          MessageType messageType)
        throws IncompatibleMessageSelectorException
    {
        return getServices().getMessagingManager().register( messageSink,
                                                             messageType );
    }

    /** Retrieve the <code>ProcessDeployemtn</code> for
     *  a specific process.
     *
     *  @param processId The process id.
     *
     *  @return The process-deployment.
     *
     *  @throws NoSuchProcessException If the <code>processId</code>
     *          does not refer to a valid deployed process.
     */
    ProcessDeployment getProcessDeployment(String processId)
        throws NoSuchProcessException
    {
        ProcessDeployment deployment = (ProcessDeployment) this.deployments.get( processId );

        if ( deployment == null )
        {
            throw new NoSuchProcessException( processId );
        }

        return deployment;
    }

    /** Evaluate the current state of a <code>WorkflowProcessCase</code>.
     *
     *  @param processCase The process-case to evaluate.
     *
     *  @throws NoSuchProcessException If the case-state is associated with
     *          a process not currently deployed within the engine.
     */
    void evaluateCase(WorkflowProcessCase processCase)
        throws NoSuchProcessException
    {
        synchronized ( processCase )
        {
            if ( processCase.hasMark( "out" ) )
            {
                processCase.setEnabledTransitions( Transition.EMPTY_ARRAY );
                notifyCaseTerminated( processCase.getProcessInfo().getId(),
                                      processCase.getId() );
                
                this.cases.remove( processCase.getId() );
                
                processCase.getState().store();
                
                return;
            }
            
            ProcessDeployment deployment = getProcessDeployment( processCase.getProcessInfo().getId() );
            
            deployment.evaluateCase( processCase );
            
            getActivityManager().scheduleCase( processCase );
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the <code>ActivityManager</code>.
     *
     *  @return The activity-manager.
     */
    ActivityManager getActivityManager()
    {
        return this.activityManager;
    }

    /** Retrieve all in-progress <code>Activity</code>s for
     *  a process case.
     *
     *  @param caseId The case identifier.
     *
     *  @return The possibly empty array of in-progress activities.
     *
     *  @throws NoSuchCaseException If no case is associated with
     *          the specified case identifier.
     *  @throws NoSuchProcessException If the process case's process is not
     *          deployed.
     */
    Activity[] getActivitiesForProcessCase(String caseId)
        throws NoSuchCaseException, NoSuchProcessException
    {
        return getActivityManager().getActivitiesForProcessCase( caseId );
    }

    /** Create a new <code>CaseState</code>.
     *
     *  @param processId The process identifier.
     *  @param attributes The initial case attributes.
     *
     *  @return The new case state.
     */
    CaseState newCaseState(String processId,
                           Attributes attributes)
    {
        return getServices().getCaseRepository().newCaseState( processId,
                                                               attributes );
    }

    /** Retrieve the <code>CaseState</code> for a process-case.
     *
     *  @param caseId The case identifier.
     *
     *  @return The case state or <code>null</code> if none is associated
     *          with the specified identifier.
     */
    CaseState getCaseState(String caseId)
    {
        return getServices().getCaseRepository().getCaseState( caseId );
    }

    /** Consume a message.
     *
     *  @param processCase The case consuming the message.
     *  @param transition The transition consuming the message.
     *
     *  @return The consumed message.  
     *
     *  @throws NoSuchCorrelationException If the message does not correlate
     *          to the process case requesting the consumption.
     *  @throws NoSuchProcessException If the process case's process is not
     *          deployed.
     */
    Object consumeMessage(WorkflowProcessCase processCase,
                          Transition transition)
        throws NoSuchCorrelationException, NoSuchProcessException
    {
        ProcessDeployment deployment = getProcessDeployment( processCase.getProcessInfo().getId() );
        
        return deployment.consumeMessage( processCase.getId(),
                                          transition );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     event listener
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Attach a <code>WfmsEventListener</code>.
     *
     *  @param listener The listener
     */
    void addEventListener(WfmsEventListener listener)
    {
        this.listeners.add( listener );
    }

    /** Notify listeners of a deployed process.
     *
     *  @param processDef The deployed process.
     */
    void notifyProcessDeployed(ProcessDefinition processDef)
    {
        ProcessDeployedEvent event = new ProcessDeployedEvent( this,
                                                               processDef );

        Iterator          listenerIter = this.listeners.iterator();
        WfmsEventListener eachListener = null;

        while ( listenerIter.hasNext() )
        {
            eachListener = (WfmsEventListener) listenerIter.next();

            eachListener.processDeployed( event );
        }
    }

    /** Notify listeners of an undeployed process.
     *
     *  @param processId The undeployed process identifier.
     */
    void notifyProcessUndeployed(String processId)
    {
        ProcessUndeployedEvent event = new ProcessUndeployedEvent( this,
                                                                   processId );

        Iterator          listenerIter = this.listeners.iterator();
        WfmsEventListener eachListener = null;

        while ( listenerIter.hasNext() )
        {
            eachListener = (WfmsEventListener) listenerIter.next();

            eachListener.processUndeployed( event );
        }
    }

    /** Notify listeners of an initiated process case.
     *
     *  @param processId The process identifier.
     *  @param caseId The case identifier.
     */
    void notifyCaseInitiated(String processId,
                             String caseId)
    {
        CaseInitiatedEvent event = new CaseInitiatedEvent( this,
                                                           processId,
                                                           caseId );

        Iterator          listenerIter = this.listeners.iterator();
        WfmsEventListener eachListener = null;

        while ( listenerIter.hasNext() )
        {
            eachListener = (WfmsEventListener) listenerIter.next();

            eachListener.caseInitiated( event );
        }
    }

    /** Notify listeners of a terminated process case.
     *
     *  @param processId The process identifier.
     *  @param caseId The case identifier.
     */
    void notifyCaseTerminated(String processId,
                              String caseId)
    {
        CaseTerminatedEvent event = new CaseTerminatedEvent( this,
                                                             processId,
                                                             caseId );
        
        Iterator          listenerIter = this.listeners.iterator();
        WfmsEventListener eachListener = null;

        while ( listenerIter.hasNext() )
        {
            eachListener = (WfmsEventListener) listenerIter.next();

            eachListener.caseTerminated( event );
        }
    }

    /** Notify listeners of an initiated transition.
     *
     *  @param processId The process identifier.
     *  @param caseId The case identifier.
     *  @param transitionId The transition identifier.
     */
    void notifyTransitionInitiated(String processId,
                                   String caseId,
                                   String transitionId)
    {
        TransitionInitiatedEvent event = new TransitionInitiatedEvent( this,
                                                                       processId,
                                                                       caseId,
                                                                       transitionId );
        
        Iterator          listenerIter = this.listeners.iterator();
        WfmsEventListener eachListener = null;

        while ( listenerIter.hasNext() )
        {
            eachListener = (WfmsEventListener) listenerIter.next();

            eachListener.transitionInitiated( event );
        }
    }

    /** Notify listeners of a terminated transition.
     *
     *  @param processId The process identifier.
     *  @param caseId The case identifier.
     *  @param transitionId The transition identifier.
     */
    void notifyTransitionTerminated(String processId,
                                    String caseId,
                                    String transitionId)
    {
        TransitionTerminatedEvent event = new TransitionTerminatedEvent( this,
                                                                         processId,
                                                                         caseId,
                                                                         transitionId );
        
        Iterator          listenerIter = this.listeners.iterator();
        WfmsEventListener eachListener = null;

        while ( listenerIter.hasNext() )
        {
            eachListener = (WfmsEventListener) listenerIter.next();

            eachListener.transitionTerminated( event );
        }
    }

    /** Notify listeners of a erroneously terminated transition.
     *
     *  @param processId The process identifier.
     *  @param caseId The case identifier.
     *  @param transitionId The transition identifier.
     *  @param error The error.
     */
    void notifyTransitionTerminated(String processId,
                                    String caseId,
                                    String transitionId,
                                    Throwable error)
    {
        TransitionTerminatedEvent event = new TransitionTerminatedEvent( this,
                                                                         processId,
                                                                         caseId,
                                                                         transitionId,
                                                                         error );
        
        Iterator          listenerIter = this.listeners.iterator();
        WfmsEventListener eachListener = null;

        while ( listenerIter.hasNext() )
        {
            eachListener = (WfmsEventListener) listenerIter.next();

            eachListener.transitionTerminated( event );
        }
    }

    /** Notify listeners of tokens produced.
     *
     *  @param processId The process identifier.
     *  @param caseId The case identifier.
     *  @param transitionId The transition identifier.
     *  @param placeIds The identifiers of the places with affect tokens.
     */
    void notifyTokensProduced(String processId,
                              String caseId,
                              String transitionId,
                              String[] placeIds)
    {
        TokensProducedEvent event = new TokensProducedEvent( this,
                                                             processId,
                                                             caseId,
                                                             transitionId,
                                                             placeIds );
        
        Iterator          listenerIter = this.listeners.iterator();
        WfmsEventListener eachListener = null;

        while ( listenerIter.hasNext() )
        {
            eachListener = (WfmsEventListener) listenerIter.next();

            eachListener.tokensProduced( event );
        }
    }

    /** Notify listeners of tokens consumed.
     *
     *  @param processId The process identifier.
     *  @param caseId The case identifier.
     *  @param transitionId The transition identifier.
     *  @param placeIds The identifiers of the places with affect tokens.
     */
    void notifyTokensConsumed(String processId,
                              String caseId,
                              String transitionId,
                              String[] placeIds)
    {
        TokensConsumedEvent event = new TokensConsumedEvent( this,
                                                             processId,
                                                             caseId,
                                                             transitionId,
                                                             placeIds );
        
        Iterator          listenerIter = this.listeners.iterator();
        WfmsEventListener eachListener = null;

        while ( listenerIter.hasNext() )
        {
            eachListener = (WfmsEventListener) listenerIter.next();

            eachListener.tokensConsumed( event );
        }
    }
    
    /** Notify listeners of tokens rolled back.
     *
     *  @param processId The process identifier.
     *  @param caseId The case identifier.
     *  @param transitionId The transition identifier.
     *  @param placeIds The identifiers of the places with affect tokens.
     */
    void notifyTokensRolledBack(String processId,
                                String caseId,
                                String transitionId,
                                String[] placeIds)
    {
        TokensRolledBackEvent event = new TokensRolledBackEvent( this,
                                                                 processId,
                                                                 caseId,
                                                                 transitionId,
                                                                 placeIds );
        
        Iterator          listenerIter = this.listeners.iterator();
        WfmsEventListener eachListener = null;

        while ( listenerIter.hasNext() )
        {
            eachListener = (WfmsEventListener) listenerIter.next();

            eachListener.tokensRolledBack( event );
        }
    }
}
