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

import com.werken.werkflow.QueryException;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/** Memory-based <code>CaseRepository</code>.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class InMemoryCaseRepository
    extends AbstractCaseRepository
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    /** Empty <code>String</code> array. */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Case-id counter. */
    private long counter;

    /** All cases, by id. */
    private Map cases;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------
    
    /** Construct.
     */
    public InMemoryCaseRepository()
    {
        this.counter = 0;
        this.cases   = new HashMap();
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** @see AbstractCaseRepository
     */
    protected synchronized String nextCaseId()
    {
        return "case." + (++counter);
    }

    /** @see AbstractCaseRepository
     */
    protected void store(CaseState state)
    {
        this.cases.put( state.getCaseId(),
                        state );
    }

    /** @see AbstractCaseRepository
     */
    protected CaseState retrieve(String caseId)
    {
        return (CaseState) this.cases.get( caseId );
    }

    /** @see CaseRepository
     */
    public String[] selectCases(String processId,
                                String placeId)
        throws QueryException
    {
        List caseIds = new ArrayList();
        
        Iterator  caseIter = this.cases.values().iterator();
        CaseState eachCase = null;

        while ( caseIter.hasNext() )
        {
            eachCase = (CaseState) caseIter.next();

            if ( eachCase.getProcessId().equals( processId ) )
            {
                if ( eachCase.hasMark( placeId ) )
                {
                    caseIds.add( eachCase.getCaseId() );
                }
            }
        }

        return (String[]) caseIds.toArray( EMPTY_STRING_ARRAY );
    }

    /** @see CaseRepository
     */
    public String[] selectCases(String processId,
                                Map qbeAttrs)
    {
        List caseIds = new ArrayList();
        
        Iterator  caseIter = this.cases.values().iterator();
        CaseState eachCase = null;

        while ( caseIter.hasNext() )
        {
            eachCase = (CaseState) caseIter.next();

            if ( eachCase.getProcessId().equals( processId ) )
            {
                if ( evalQueryByExample( eachCase,
                                         qbeAttrs ) )
                {
                    caseIds.add( eachCase.getCaseId() );
                }
            }
        }

        return (String[]) caseIds.toArray( EMPTY_STRING_ARRAY );
    }

    /** Evaluate the query-by-example attributes against a <code>CaseState</code>.
     *
     *  @param caseState The case state to evaluate.
     *  @param qbeAttrs The query-by-example attributes.
     *
     *  @return <code>true</code> if the query matches, otherwise <code>false</code>.
     */
    protected boolean evalQueryByExample(CaseState caseState,
                                         Map qbeAttrs)
    {
        Iterator attrNameIter = qbeAttrs.keySet().iterator();
        String   eachAttrName = null;

        while ( attrNameIter.hasNext() )
        {
            eachAttrName = (String) attrNameIter.next();

            if ( ! caseState.hasAttribute( eachAttrName ) )
            {
                return false;
            }

            if ( ! caseState.getAttribute( eachAttrName ).equals( qbeAttrs.get( eachAttrName ) ) )
            {
                return false;
            }
        }

        return true;
    }
}
