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

import com.werken.werkflow.NoSuchCaseException;
import com.werken.werkflow.NoSuchProcessException;
import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.MessageCorrelator;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.messaging.Message;
import com.werken.werkflow.service.messaging.NoSuchMessageException;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;

/** Correlator of <code>Message</code>s to <code>MessageWaiter</code>s.
 *
 *  <p>
 *  Each <code>Transition</code> of a process that includes a <code>MessageWaiter</code>
 *  results in a <code>MessageWaiterCorrelator</code> attempting to correlate
 *  messages to cases waiting at the transition.
 *  </p>
 *
 *  @see Correlator
 *  @see MessageTypeCorrelator
 *  @see Message
 *  @see MessageWaiter
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class MessageWaiterCorrelator
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Workflow engine. */
    private WorkflowEngine engine;

    /** Owning message-type correlator. */
    private MessageTypeCorrelator msgTypeCorrelator;

    /** Transition identifier. */
    private String transitionId;

    /** Transition message-waiter. */
    private MessageWaiter messageWaiter;

    /** Message identifiers. */
    private List messageIds;

    /** Waiting case identifiers. */
    private List caseIds;

    /** Current correlations. */
    private List correlations;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param engine The workflow engine.
     *  @param msgTypeCorrelator The owning message-type correlator.
     *  @param transitionId The transition identifier.
     *  @param messageWaiter The transition's message-waiter.
     */
    public MessageWaiterCorrelator(WorkflowEngine engine,
                                   MessageTypeCorrelator msgTypeCorrelator,
                                   String transitionId,
                                   MessageWaiter messageWaiter)
    {
        this.engine            = engine;
        this.msgTypeCorrelator = msgTypeCorrelator;
        this.transitionId      = transitionId;
        this.messageWaiter     = messageWaiter;

        this.messageIds    = new LinkedList();
        this.caseIds       = new LinkedList();
        this.correlations  = new LinkedList();
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

    /** Retrieve the owning <code>MessageTypeCorrelator</code>.
     *
     *  @return The message-type correlator.
     */
    private MessageTypeCorrelator getMessageTypeCorrelator()
    {
        return this.msgTypeCorrelator;
    }

    /** Retrieve the transition identifier.
     *
     *  @return The transition identifier.
     */
    public String getTransitionId()
    {
        return this.transitionId;
    }

    /** Retrieve the <code>MessageWaiter</code>.
     *
     *  @return The message-waiter.
     */
    public MessageWaiter getMessageWaiter()
    {
        return this.messageWaiter;
    }

    /** Retrieve a <code>Message</code> by identifier.
     *
     *  @param msgId The message identifier.
     *
     *  @return The message.
     *
     *  @throws NoSuchMessageException If no message is associated
     *          with the specified identifier.
     */
    Message getMessage(String msgId)
        throws NoSuchMessageException
    {
        return getMessageTypeCorrelator().getMessage( msgId );
    }

    /** Accept a new <code>Message</code>.
     *
     *  @param message The message.
     */
    public synchronized void acceptMessage(Message message)
    {
        addMessage( message.getId() );

        List reevaluate = new ArrayList();

        Iterator caseIdIter = this.caseIds.iterator();
        String   eachCaseId = null;
        WorkflowProcessCase eachCase = null;

        while ( caseIdIter.hasNext() )
        {
            eachCaseId = (String) caseIdIter.next();

            try
            {
                eachCase = getEngine().getProcessCase( eachCaseId );

                if ( attemptCorrelation( message,
                                         eachCase ) )
                {
                    reevaluate.add( eachCase );
                }
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

        Iterator            reevalIter = reevaluate.iterator();
        WorkflowProcessCase eachReeval = null;

        while ( reevalIter.hasNext() )
        {
            eachReeval = (WorkflowProcessCase) reevalIter.next();

            try
            {
                getEngine().evaluateCase( eachReeval );
            }
            catch (NoSuchProcessException e)
            {
                // FIXME
                e.printStackTrace();
            }
        }
    }

    /** Remove a message.
     *
     *  @param messageId The message to remove.
     */
    void removeMessage(String messageId)
    {
        while ( this.messageIds.remove( messageId ) )
        {
            // intentionally left blank
        }

        Iterator    correlationIter = this.correlations.iterator();
        Correlation eachCorrelation = null;

        while ( correlationIter.hasNext() )
        {
            eachCorrelation = (Correlation) correlationIter.next();

            if ( eachCorrelation.getMessageId().equals( messageId ) )
            {
                correlationIter.remove();
            }
        }
    }

    /** Add a message.
     *
     *  @param messageId The message to add.
     */
    void addMessage(String messageId)
    {
        this.messageIds.add( messageId );
    }

    /** Accept a <code>WorkflowProcessCase</code> for processing.
     *
     *  @param processCase The process case.
     */
    public synchronized void acceptProcessCase(WorkflowProcessCase processCase)
    {
        if ( containsProcessCase( processCase.getId() ) )
        {
            return;
        }

        addProcessCase( processCase.getId() );

        Iterator msgIdIter = this.messageIds.iterator();
        String   eachMsgId = null;

        Message eachMsg = null;

        while ( msgIdIter.hasNext() )
        {
            eachMsgId = (String) msgIdIter.next();

            try
            {
                eachMsg = getMessage( eachMsgId );

                attemptCorrelation( eachMsg,
                                    processCase );
            }
            catch (NoSuchMessageException e)
            {
                // FIXME
                e.printStackTrace();
            }
        }
    }

    /** Remove a process case.
     *
     *  @param processCaseId The process case identifier.
     */
    void removeProcessCase(String processCaseId)
    {
        while ( this.caseIds.remove( processCaseId ) )
        {
            // intentionally left blank
        }

        Iterator    correlationIter = this.correlations.iterator();
        Correlation eachCorrelation = null;

        while ( correlationIter.hasNext() )
        {
            eachCorrelation = (Correlation) correlationIter.next();

            if ( eachCorrelation.getProcessCaseId().equals( processCaseId ) )
            {
                correlationIter.remove();
            }
        }
    }

    /** Add a process case.
     *
     *  @param processCaseId The process case identifier.
     */
    void addProcessCase(String processCaseId)
    {
        this.caseIds.add( processCaseId );
    }

    /** Determine if a process-case is contained by the
     *  correlator and waiting for correlation.
     *
     *  @param processCaseId The process case identifier.
     *
     *  @return <code>true</code> if the process case is considered
     *          for correlation, otherwise <code>false</code>.
     */
    boolean containsProcessCase(String processCaseId)
    {
        return this.caseIds.contains( processCaseId );
    }

    /** Attempt to correlate a <code>Message</code>
     *  and a <code>WorkflowProcessCase</code>.
     *
     *  @param message The message.
     *  @param processCase The processCase.
     *
     *  @return <code>true</code> if the message and process case
     *          successfully correlate, otherwise <code>false</code>.
     */
    boolean attemptCorrelation(Message message,
                               WorkflowProcessCase processCase)
    {
        try
        {
            MessageCorrelator msgCorrelator = getMessageWaiter().getMessageCorrelator();

            boolean doesCorrelate = true;

            if ( msgCorrelator != null )
            {
                doesCorrelate = msgCorrelator.correlates( message.getMessage(),
                                                   processCase );
            }
            
            if ( doesCorrelate )
            {
                notifyCorrelation( message,
                                   processCase );
                
                return true;
            }
        }
        catch (Exception e)
        {
            // FIXME
            e.printStackTrace();
        }

        return false;
    }

    /** Notify of a new successful correlation.
     *
     *  @param message The message.
     *  @param processCase The process case.
     */
    void notifyCorrelation(Message message,
                           WorkflowProcessCase processCase)
    {
        this.correlations.add( new Correlation( message.getId(),
                                                processCase.getId() ) );
    }

    /** Evalaute a case for potential correlations.
     *
     *  @param processCase The process case.
     *  @param transitionIds The activated transition identifiers.
     */
    synchronized void evaluateCase(WorkflowProcessCase processCase,
                                   String[] transitionIds)
    {
        for ( int i = 0 ; i < transitionIds.length ; ++i )
        {
            if ( transitionIds[i].equals( this.transitionId ) )
            {
                acceptProcessCase( processCase );
                return;
            }
        }

        Transition[] enabledTransitions = processCase.getEnabledTransitions();

        for ( int i = 0 ; i < enabledTransitions.length ; ++i )
        {
            if ( enabledTransitions[i].getId().equals( this.transitionId ) )
            {
                return;
            }
        }

        // if not accepted, then it's not correlating or
        // correlatable to anything here, so remove it completely
        // and remove all correlations.

        String processCaseId = processCase.getId();

        removeProcessCase( processCaseId );
    }

    /** Determine if a process case has at least one correlation.
     *
     *  @param processCaseId The process case identifier.
     *
     *  @return <code>true</code> if a message correlates with the 
     *          process case, otherwise <code>false</code>.
     */
    boolean isCorrelated(String processCaseId)
    {
        Iterator    correlationIter = this.correlations.iterator();
        Correlation eachCorrelation = null;

        while ( correlationIter.hasNext() )
        {
            eachCorrelation = (Correlation) correlationIter.next();

            if ( eachCorrelation.getProcessCaseId().equals( processCaseId ) )
            {
                return true;
            }
        }

        return false;
    }

    /** Consume the next correlated message for a process case.
     *
     *  @see #isCorrelated
     *
     *  @param processCaseId The process case identifier.
     *
     *  @return The consumed message object.
     *
     *  @throws NoSuchCorrelationException If no message correlates
     *          with the process case.
     */
    synchronized Object consumeMessage(String processCaseId)
        throws NoSuchCorrelationException
    {
        Iterator    correlationIter = this.correlations.iterator();
        Correlation eachCorrelation = null;

        Object returnMsg = null;

        while ( correlationIter.hasNext() )
        {
            eachCorrelation = (Correlation) correlationIter.next();

            if ( eachCorrelation.getProcessCaseId().equals( processCaseId ) )
            {
                try
                {
                    Message message = getMessage( eachCorrelation.getMessageId() );
                    
                    removeMessage( message.getId() );
                    
                    returnMsg = message.getMessage();

                    break;
                }
                catch (NoSuchMessageException e)
                {
                    // FIXME
                    e.printStackTrace();
                }
            }
        }

        if ( returnMsg == null )
        {
            throw new NoSuchCorrelationException( processCaseId,
                                                  getTransitionId() );
        }

        removeProcessCase( processCaseId );

        return returnMsg;
    }
}
