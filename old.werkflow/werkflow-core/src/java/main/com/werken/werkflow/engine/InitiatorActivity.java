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

import com.werken.werkflow.SimpleAttributes;
import com.werken.werkflow.NoSuchProcessException;
import com.werken.werkflow.activity.Activity;

import java.util.Map;

/** <code>Activity</code> for process-initiating actions.
 *
 *  @see Initiator
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
class InitiatorActivity
    implements Activity
{
    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Error, possibly null. */
    private Throwable error;

    /** Workflow engine. */
    private WorkflowEngine engine;

    /** Process identifier of process to initiate. */
    private String processId;

    /** Initial attributes backing storage. */
    private Map initialAttrs;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param engine The workflow engine.
     *  @param processId The identifier of the process to initiate.
     *  @param initialAttrs The map backing the initial attributes.
     */
    public InitiatorActivity(WorkflowEngine engine,
                             String processId,
                             Map initialAttrs)
    {
        this.engine       = engine;
        this.processId    = processId;
        this.initialAttrs = initialAttrs;
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

    /** @see Activity
     */
    public String getCaseId()
    {
        return null;
    }

    /** @see Activity
     */
    public String getTransitionId()
    {
        return null;
    }

    /** @see Activity
     */
    public String getProcessId()
    {
        return this.processId;
    }

    /** Retrieve the initial attributes backing map.
     *
     *  @return The initial attributes backing map.
     */
    private Map getInitialAttributes()
    {
        return this.initialAttrs;
    }

    /** @see Activity
     */
    public void complete()
    {
        SimpleAttributes initialAttrs = new SimpleAttributes( getInitialAttributes() );

        try
        {
            getEngine().newProcessCase( getProcessId(),
                                        initialAttrs );
        }
        catch (NoSuchProcessException e)
        {
            completeWithError( e );
        }
    }

    /** @see Activity
     */
    public void completeWithError(Throwable error)
    {
        this.error = error;
    }

    /** @see Activity
     */
    public Throwable getError()
    {
        return this.error;
    }
}
