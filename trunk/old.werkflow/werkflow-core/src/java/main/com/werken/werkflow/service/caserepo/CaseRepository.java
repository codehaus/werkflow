package com.werken.werkflow.service.caserepo;

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
import com.werken.werkflow.QueryException;

import java.util.Map;

/** Service to provide persistence for process cases.
 *
 *  <p>
 *  <code>CaseRepository</code> implementations are responsible
 *  for collaborating with <code>CaseState</code> implementations
 *  to persistently store and reliably retrieve the state of process
 *  cases.
 *  </p>
 *  
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public interface CaseRepository
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    /** Component role.
     */
    public static final String ROLE = CaseRepository.class.getName();

    
    // ----------------------------------------------------------------------
    //     Interface
    // ----------------------------------------------------------------------

    /** Create a new persistent case state for a given process
     *  with initial attribute values.
     *
     *  @param processId The identifier of the process for which to
     *         instantiate and store a new case-state.
     *  @param attributes The case's initial attributes to be stored.
     */
    CaseState newCaseState(String processId,
                           Attributes attributes);

    /** Retrieve a case's state by identifier.
     *
     *  @param caseId The case identifier.
     *
     *  @return The retrieved state or <code>null</code> indicating
     *          that such state could not be retrieved.
     */
    CaseState getCaseState(String caseId);

    String[] selectCases(String processId,
                         String placeId)
        throws QueryException;

    String[] selectCases(String processId,
                         Map qbeAttrs)
        throws QueryException;
}
