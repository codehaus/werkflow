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

import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.AttributeDeclaration;
import com.werken.werkflow.definition.petri.Transition;
import com.werken.werkflow.service.caserepo.CaseState;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/** Process instance.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class WorkflowProcessCase
    implements ProcessCase
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    /** Empty <code>WorkflowProcessCase</code> array. */
    public static final WorkflowProcessCase[] EMPTY_ARRAY = new WorkflowProcessCase[0];

    /** Empty <code>String</code> array. */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Process info. */
    private ProcessInfo info;

    /** Backing state. */
    private CaseState state;

    /** Currently enabled transitions. */
    private Transition[] enabledTransitions;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param info The process-info.
     *  @param state The backing state.
     */
    public WorkflowProcessCase(ProcessInfo info,
                               CaseState state)
    {
        this.info  = info;
        this.state = state;
        this.enabledTransitions = Transition.EMPTY_ARRAY;
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Retrieve the backing <code>CaseState</code>.
     *
     *  @return The backing state.
     */
    protected CaseState getState()
    {
        return this.state;
    }

    /** @see ProcessCase
     */
    public String getId()
    {
        return getState().getCaseId();
    }

    /** @see ProcessCase
     */
    public ProcessInfo getProcessInfo()
    {
        return this.info;
    }

    /** @see Attributes
     */
    public Object getAttribute(String name)
    {
        return getState().getAttribute( name );
    }

    /** @see Attributes
     */
    public void setAttribute(String name,
                             Object value)
    {
        getState().setAttribute( name,
                                 value );
    }

    /** @see Attributes
     */
    public void clearAttribute(String name)
    {
        getState().clearAttribute( name );
    }

    /** @see Attributes
     */
    public String[] getAttributeNames()
    {
        return getState().getAttributeNames();
    }

    /** @see Attributes
     */
    public boolean hasAttribute(String name)
    {
        return getState().hasAttribute( name );
    }

    /** Determine if the case has a mark at a specific place.
     *
     *  @param placeId The place identifier.
     *
     *  @return <code>true</code> if the place contains a mark,
     *          otherwise <code>false</code>.
     */
    public boolean hasMark(String placeId)
    {
        return getState().hasMark( placeId );
    }

    /** Retrieve the identifiers of all marked places.
     *
     *  @return The array of identifiers of all marked places.
     */
    public String[] getMarks()
    {
        return getState().getMarks();
    }

    /** Add a mark to a place.
     *
     *  @param placeId The place identifier.
     */
    public void addMark(String placeId)
    {
        getState().addMark( placeId );
    }

    /** Remove a mark from a place.
     *
     *  @param placeId The place identifier.
     */
    public void removeMark(String placeId)
    {
        getState().removeMark( placeId );
    }

    /** Set the currently enabled transitions.
     *
     *  @param enabledTransitions The enabled transitions.
     */
    public void setEnabledTransitions(Transition[] enabledTransitions)
    {
        this.enabledTransitions = enabledTransitions;
    }

    /** Retrieve the currently enabled transitions.
     *
     *  @return The enabled transitions.
     */
    public Transition[] getEnabledTransitions()
    {
        return this.enabledTransitions;
    }

    /** Set all case attributes.
     *
     *  @param caseAttrs The new case attributes.
     */
    void setCaseAttributes(Map caseAttrs)
    {
        String[] origAttrNames = getAttributeNames();

        for ( int i = 0 ; i < origAttrNames.length ; ++i )
        {
            if ( ! caseAttrs.containsKey( origAttrNames[i] ) )
            {
                clearAttribute( origAttrNames[i] );
            }
        }

        Iterator attrNames   = caseAttrs.keySet().iterator();
        String  eachAttrName = null;

        while ( attrNames.hasNext() )
        {
            eachAttrName = (String) attrNames.next();

            setAttribute( eachAttrName,
                          caseAttrs.get( eachAttrName ) );
        }
    }

    /** Retrieve all case attributes.
     *
     *  @return The case attributes.
     */
    Map getCaseAttributes()
    {
        Map caseAttrs = new HashMap();

        String[] attrNames = getAttributeNames();

        for ( int i = 0 ; i < attrNames.length ; ++i )
        {
            caseAttrs.put( attrNames[i],
                           getAttribute( attrNames[i] ) );
        }

        return caseAttrs;
    }

    void mergeCaseAttributes(Map attrs)
    {
        Iterator nameIter = attrs.keySet().iterator();
        String   eachName = null;

        while ( nameIter.hasNext() )
        {
            eachName = (String) nameIter.next();

            setAttribute( eachName,
                          attrs.get( eachName ) );
        }
    }
}
