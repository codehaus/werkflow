package com.werken.werkflow.definition.petri;

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

import com.werken.werkflow.engine.WorkflowProcessCase;

import java.util.List;
import java.util.ArrayList;

/** And-based <code>ActiviationRule</code> for a <code>Transition</code>.
 *
 *  <p>
 *  This <code>ActivationRule</code> implementation requires that all
 *  <code>Place</code> inputs for the <code>Transition</code> contain
 *  a valid mark.
 *  </p>
 *
 *  @see ActivationRule
 *  @see OrInputRule
 *  @see Transition#getActivationRule
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class AndInputRule
    implements ActivationRule
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    /** Empty <code>String</code> array. */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /** Singleton instance. */
    private static final AndInputRule INSTANCE = new AndInputRule();

    // ----------------------------------------------------------------------
    //     Class methods
    // ----------------------------------------------------------------------

    /** Retrieve the singleton instance.
     *
     *  @return The singleton instance.
     */
    public static ActivationRule getInstance()
    {
        return INSTANCE;
    }
    
    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    private AndInputRule()
    {
        // intentionally left blank
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** @see ActivationRule
     */
    public boolean isSatisfied(Transition transition,
                               WorkflowProcessCase processCase)
        throws Exception
    {
        Arc[] arcs = transition.getArcsFromPlaces();
        Place eachPlace = null;

        for ( int i = 0 ; i < arcs.length ; ++i )
        {
            eachPlace = arcs[i].getPlace();

            if ( ! processCase.hasMark( eachPlace.getId() ) )
            {
                return false;
            }

            if ( arcs[i].getExpression() != null )
            {
                return arcs[i].getExpression().evaluate( processCase );
            }
        }
        
        return true;
    }

    /** @see ActivationRule
     */
    public String[] satisfy(Transition transition,
                        WorkflowProcessCase processCase)
    {
        List placeIds = new ArrayList();

        Arc[] arcs = transition.getArcsFromPlaces();
        Place eachPlace = null;

        for ( int i = 0 ; i < arcs.length ; ++i )
        {
            eachPlace = arcs[i].getPlace();

            processCase.removeMark( eachPlace.getId() );
            placeIds.add( eachPlace.getId() );
        }

        return (String[]) placeIds.toArray( EMPTY_STRING_ARRAY );
    }
}
