package com.werken.werkflow.syntax.petri;

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

import com.werken.werkflow.definition.petri.DefaultNet;
import com.werken.werkflow.definition.petri.DefaultArc;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.semantics.jelly.JellyExpression;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.expression.Expression;

/** Create an output arc for a <code>Transition</code>.
 *
 *  <p>
 *  Used within a &lt;transition&gt; tag, the &lt;output&gt; tag
 *  is used to specify a &lt;place&gt; from which tokens are
 *  consumed.
 *  </p>
 *
 *  <p>
 *  <pre>
 *    &lt;place id="some.place"/&gt;
 *
 *    &lt;transition&gt;
 *      &lt;output to="some.place"/&gt;
 *    &lt;/transition&gt;
 *  </pre>
 *  </p>
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @see TransitionTag
 *  @see InputTag
 *
 *  @version $Id$
 */
public class OutputTag
    extends PetriTagSupport
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** To place identifier. */
    private String to;

    /** Mark-generation test expression. */
    private Expression testExpr;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public OutputTag()
    {
        // intentionally left blank
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Set the identifier of the <code>Place</code> to which tokens
     *  should be produced.
     *
     *  @param to The place identifier.
     */
    public void setTo(String to)
    {
        this.to = to;
    }

    /** Retrieve the identifier of the <code>Place</code> to which tokens
     *  should be produced.
     *
     *  @return The place identifier.
     */
    public String getTo()
    {
        return this.to;
    }

    /** Set the mark-generation test <code>Expression</code>.
     *
     *  @param testExpr The test expression.
     */
    public void setTest(Expression testExpr)
    {
        this.testExpr = testExpr;
    }

    /** Retrieve the mark-generation test <code>Expression</code>.
     *
     *  @return The test expression.
     */
    public Expression getTest()
    {
        return this.testExpr;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** @see org.apache.commons.jelly.Tag
     */
    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        DefaultNet net = getCurrentNet();
        DefaultTransition transition = getCurrentTransition();

        requireStringAttribute( "to",
                                getTo() );
            
        DefaultArc arc = null;

        try
        {
            arc = net.connectTransitionToPlace( transition.getId(),
                                                getTo() );
        }
        catch (Exception e)
        {
            throw new JellyTagException( e );
        }

        if ( getTest() != null )
        {
            arc.setExpression( new JellyExpression( getTest() ) );
        }
        
        invokeBody( output );
    }
}
