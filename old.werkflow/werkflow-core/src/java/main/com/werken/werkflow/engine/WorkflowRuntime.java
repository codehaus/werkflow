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
import com.werken.werkflow.WfmsRuntime;
import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.NoSuchProcessException;
import com.werken.werkflow.NoSuchCaseException;
import com.werken.werkflow.QueryException;
import com.werken.werkflow.activity.Activity;

import java.util.Map;

/** <code>WfmsRuntime</code> facade for the <code>WorkflowEngine</code>.
 *
 *  @see WorkflowEngine
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id; $
 */
class WorkflowRuntime
    implements WfmsRuntime
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Wrapped workflow engine. */
    private WorkflowEngine engine;


    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------
    
    /** Constrct a new wrapper for a <code>WorkflowEngine</code>.
     *
     *  @param engine The engine to wrap.
     */
    public WorkflowRuntime(WorkflowEngine engine)
    {
        this.engine = engine;
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Retrieve the wrapped <code>WorkflowEngine</code>.
     *
     *  @return The wrapp workflow-engine.
     */
    public WorkflowEngine getEngine()
    {
        return this.engine;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** @see WfmsRuntime
     */
    public ProcessInfo[] getProcesses()
    {
        return getEngine().getProcesses();
    }

    /** @see WfmsRuntime
     */
    public ProcessInfo getProcess(String processId)
        throws NoSuchProcessException
    {
        return getEngine().getProcess( processId );
    }

    /** @see WfmsRuntime
     */
    public ProcessCase getProcessCase(String caseId)
        throws NoSuchProcessException, NoSuchCaseException
    {
        return getEngine().getProcessCase( caseId );
    }

    /** @see WfmsRuntime
     */
    public ProcessCase newProcessCase(String processId,
                                      Attributes attributes)
        throws NoSuchProcessException
    {
        return getEngine().newProcessCase( processId,
                                           attributes );
    }

    /** @see WfmsRuntime
     */
    public Activity[] getActivitiesForProcessCase(String caseId)
        throws NoSuchCaseException, NoSuchProcessException
    {
        return getEngine().getActivitiesForProcessCase( caseId );
    }

    /** @see WfmsRuntime
     */
    public ProcessCase[] selectCases(String processId,
                                     String placeId)
        throws QueryException
    {
        return getEngine().selectCases( processId,
                                        placeId );
    }

    /** @see WfmsRuntime
     */
    public ProcessCase[] selectCases(String processId,
                                     Map qbeAttrs)
        throws QueryException
    {
        return getEngine().selectCases( processId,
                                        qbeAttrs );
    }
}
