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

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/** Default <code>CaseState</code> implementation.
 *
 *  @see AbstractCaseRepository
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class DefaultCaseState
    implements CaseState
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    /** Empty <code>String</code> array. */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------
    
    /** Owning repository. */
    private transient AbstractCaseRepository repo;

    /** Case identifier. */
    private String caseId;

    /** Process identifier. */
    private String processId;

    /** Attributes. */
    private Map attributes;

    /** Petri net marks. */
    private Set marks;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------
    
    /** Construct.
     *
     *  @param caseId The case identifier.
     *  @param processId The process identifier.
     *  @param repo The owning repository.
     */
    public DefaultCaseState(String caseId,
                            String processId,
                            AbstractCaseRepository repo)
    {
        this.caseId     = caseId;
        this.processId  = processId;
        this.repo       = repo;
        this.attributes = new HashMap(); 
        this.marks      = new HashSet();
    }

    /** Copy construct.
     *
     *  @param state The case state.
     *  @param repo The new repository.
     */
    public DefaultCaseState(CaseState state,
                            AbstractCaseRepository repo)
    {
        this( state.getCaseId(),
              state.getProcessId(),
              repo );

        String[] attrNames = state.getAttributeNames();
        
        for ( int i = 0 ; i < attrNames.length ; ++i )
        {
            this.attributes.put( attrNames[i],
                                 state.getAttribute( attrNames[i] ) );
        }

        String[] marks = state.getMarks();

        for ( int i = 0 ; i < marks.length ; ++i )
        {
            this.marks.add( marks[i] );
        }
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** @see CaseState
     */
    public String getCaseId()
    {
        return this.caseId;
    }

    /** @see CaseState
     */
    public String getProcessId()
    {
        return this.processId;
    }

    /** @see CaseState
     */
    public void setAttribute(String key,
                             Object value)
    {
        this.attributes.put( key,
                             value );
    }

    /** @see CaseState
     */
    public Object getAttribute(String key)
    {
        return this.attributes.get( key );
    }

    /** @see CaseState
     */
    public String[] getAttributeNames()
    {
        return (String[]) this.attributes.keySet().toArray( EMPTY_STRING_ARRAY );
    }

    /** @see CaseState
     */
    public boolean hasAttribute(String key)
    {
        return this.attributes.containsKey( key );
    }

    /** @see CaseState
     */
    public void clearAttribute(String key)
    {
        this.attributes.remove( key );
    }

    /** @see CaseState
     */
    public void addMark(String placeId)
    {
        this.marks.add( placeId );
    }

    /** @see CaseState
     */
    public void removeMark(String placeId)
    {
        this.marks.remove( placeId );
    }

    /** @see CaseState
     */
    public String[] getMarks()
    {
        return (String[]) this.marks.toArray( EMPTY_STRING_ARRAY );
    }

    /** @see CaseState
     */
    public boolean hasMark(String placeId)
    {
        return this.marks.contains( placeId );
    }

    /** Retrieve the owning repository.
     *
     *  @return The repository.
     */
    protected AbstractCaseRepository getRepository()
    {
        return this.repo;
    }

    /** @see CaseState
     */
    public void store()
    {
        getRepository().store( this );
    }
}
