package com.werken.werkflow.event;

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

import com.werken.werkflow.Wfms;

/** Indicates a <code>Transition</code> terminated for a <code>ProcessCase</code>.
 *
 *  @see com.werken.werkflow.ProcessCase
 *  @see com.werken.werkflow.definition.ProcessDefinition
 *  @see com.werken.werkflow.definition.petri.Net
 *  @see com.werken.werkflow.definition.petri.Transition
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class TransitionTerminatedEvent
    extends TransitionEvent
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Error, if erroneous termination. */
    private Exception error;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param wfms The originating WFMS.
     *  @param processId The process identifier.
     *  @param caseId The initiated case identifier.
     *  @param transitionId The transition identifier.
     */
    public TransitionTerminatedEvent(Wfms wfms,
                                     String processId,
                                     String caseId,
                                     String transitionId)
    {
        this( wfms,
              processId,
              caseId,
              transitionId,
              null );
    }

    /** Construct.
     *
     *  @param wfms The originating WFMS.
     *  @param processId The process identifier.
     *  @param caseId The initiated case identifier.
     *  @param transitionId The transition identifier.
     *  @param error Error if erroneously terminated.
     */
    public TransitionTerminatedEvent(Wfms wfms,
                                     String processId,
                                     String caseId,
                                     String transitionId,
                                     Exception error)
    {
        super( wfms,
               processId,
               caseId,
               transitionId );

        this.error = error;
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Retrieve the error <code>Exception</code> if any.
     *
     *  @return The exception if erroneously terminated.
     */
    public Exception getError()
    {
        return this.error;
    }

    /** Determine if the transition terminated with an error.
     *
     *  @return <code>true</code> if terminated erroneously,
     *          otherwise <code>true</code>.
     */
    public boolean terminatedWithError()
    {
        return ( this.error != null );
    }
}
