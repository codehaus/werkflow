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

import com.werken.werkflow.Attributes;
import com.werken.werkflow.expr.Expression;
import com.werken.werkflow.expr.ExpressionContext;
import com.werken.werkflow.expr.AttributesExpressionContext;

/** Or-based <code>ActivationRule</code> for a <code>Transition</code>.
 *
 *  <p>
 *  This <code>ActivationRule</code> implementation requires that only
 *  one <code>Place</code> input for the <code>Transition</code> to
 *  contain a valid mark.
 *  </p>
 *
 *  @see ActivationRule
 *  @see AndInputRule
 *  @see Transition#getActivationRule
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class OrInputRule
    extends AbstractActivationRule
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    /** Empty <code>String</code> array. */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /** Singleton instance. */
    private static final OrInputRule INSTANCE = new OrInputRule();

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
    private OrInputRule()
    {
        // intentionally left blank
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** @see ActivationRule
     */
    public String[] getSatisfyingTokens(Transition transition,
                                        Attributes caseAttrs,
                                        String[] availMarks)
        throws Exception
    {
        Arc[] arcs = transition.getArcsFromPlaces();
        String eachPlaceId = null;

        ExpressionContext context = new AttributesExpressionContext( caseAttrs );

        for ( int i = 0 ; i < arcs.length ; ++i )
        {
            eachPlaceId = arcs[i].getPlace().getId();

            if ( contains( eachPlaceId,
                           availMarks ) )
            {
                Expression expr = arcs[i].getExpression();

                if ( expr != null )
                {
                    if ( expr.evaluateAsBoolean( context ) )
                    {
                        return new String[] { eachPlaceId };
                    }
                }
                else
                {
                    return new String[] { eachPlaceId };
                }
            }
        }

        return EMPTY_STRING_ARRAY;
    }
}
