package com.werken.werkflow;

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

import com.werken.werkflow.activity.Activity;

import java.util.Map;

/** Client runtime interface.
 *
 *  <p>
 *  The <code>WfmsRuntime</code> is the main entry point
 *  for most application code that uses werkflow.  New
 *  process-cases can be created, existing cases can be
 *  retrieved, and actions can be taken via the <code>WfmsRuntime</code.
 *  </p>
 *
 *  <p>
 *  The available deployed processes and resources are
 *  controlled by the <code>WfmsAdmin</code> interface.
 *  </p>
 *
 *  @see ProcessCase
 *  @see ProcessInfo
 *  @see com.werken.werkflow.admin.WfmsAdmin
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public interface WfmsRuntime
{
    /** Retrieve the <code>ProcessInfo</code> descriptors for
     *  all available deployed processes.
     *
     *  @return The process-info descriptors.
     */
    ProcessInfo[] getProcesses();

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
        throws NoSuchProcessException;

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
    ProcessCase getProcessCase(String caseId)
        throws NoSuchCaseException, NoSuchProcessException;
    
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
    ProcessCase callProcess(String processId,
                            Attributes attributes)
        throws ProcessException;
    
    /** Retrieve the in-progress <code>Activity</code> handles
     *  for a given case.
     *
     *  @param caseId The case id.
     *
     *  @return The activities.
     *
     *  @throws NoSuchCaseException If the identifier does not
     *          match any case known by the system.
     *  @throws NoSuchProcessException If the identifier do match
     *          a case known by the system, but the process associated
     *          with the case is not currently deployed.
     */
    Activity[] getActivitiesForProcessCase(String caseId)
        throws NoSuchCaseException, NoSuchProcessException;

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
        throws QueryException;

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
        throws QueryException;
}
