package org.codehaus.werkflow.event;

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

import java.util.EventListener;

/** Listener for <code>WfmsEvent</code>s.
 *
 *  <p>
 *  Singular events are sent whenever a process is deployed
 *  or undeployed.
 *  </p>
 *
 *  <p>
 *  When a new <code>ProcessCase</code> is initiated, a
 *  <code>CaseInitiatedEvent</code> is sent, followed by a
 *  <code>TokensProducedEvent</code> indicating a new token
 *  produced at the <code>in</code> place.
 *  </p>
 *
 *  <p>
 *  When a <code>Transition</code> fires, one of two sequences of
 *  events is produced.
 *  </p>
 *
 *  <p>
 *  For successful transition-firing:
 *    <ol>
 *      <li><code>TransitionInitiatedEvent</code></li>
 *      <li><code>TokensConsumedEvent</code></li>
 *      <li><code>TokensProducedEvent</code></li>
 *      <li><code>TransitionTerminatedEvent</code></li>
 *    </ol>
 *  </p>
 *
 *  <p>
 *  For unsuccessful transition-firing:
 *    <ol>
 *      <li><code>TransitionInitiatedEvent</code></li>
 *      <li><code>TokensConsumedEvent</code></li>
 *      <li><code>TokensRolledBackEvent</code></li>
 *      <li><code>TransitionTerminatedEvent</code></li>
 *    </ol>
 *
 *  The <code>TransitionTerminatedEvent</code> will return
 *  <code>true</code> for {@link TransitionTerminatedEvent#terminatedWithError}
 *  and the error will be provided via {@link TransitionTerminatedEvent#getError}
 *  </p>
 *
 *  <p>
 *  When a process-case reaches a point that it contains
 *  a token in the <code>out</code> place, a <code>CaseTerminatedEvent</code>
 *  will be sent.
 *  </p>
 *
 *  @see org.codehaus.werkflow.admin.WfmsAdmin#addEventListener
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public interface WfmsEventListener
    extends EventListener
{
    /** Receive notification of a new process deployment.
     *
     *  @param event The event
     */
    void processDeployed(ProcessDeployedEvent event);

    /** Receive notification of a process undeployment.
     *
     *  @param event The event
     */
    void processUndeployed(ProcessUndeployedEvent event);

    /** Receive notification of a new process case initiation.
     *
     *  @param event The event
     */
    void caseInitiated(CaseInitiatedEvent event);

    /** Receive notification of a process case termination.
     *
     *  @param event The event
     */
    void caseTerminated(CaseTerminatedEvent event);

    /** Receive notification of a tokens produced.
     *
     *  @param event The event
     */
    void tokensProduced(TokensProducedEvent event);

    /** Receive notification of a tokens consumed.
     *
     *  @param event The event
     */
    void tokensConsumed(TokensConsumedEvent event);

    /** Receive notification of a tokens rolled back.
     *
     *  @param event The event
     */
    void tokensRolledBack(TokensRolledBackEvent event);

    /** Receive notification of a transition initiated.
     *
     *  @param event The event
     */
    void transitionInitiated(TransitionInitiatedEvent event);

    /** Receive notification of a transition terminated.
     *
     *  @param event The event
     */
    void transitionTerminated(TransitionTerminatedEvent event);
}
