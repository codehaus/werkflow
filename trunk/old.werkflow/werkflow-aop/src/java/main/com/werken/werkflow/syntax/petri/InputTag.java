package com.werken.werkflow.syntax.petri;

/*
 $Id$

 Copyright 2003 (C) The Codehaus. All Rights Reserved.

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
    permission of The Codehaus.  For written permission,
    please contact info@codehaus.org.

 4. Products derived from this Software may not be called "werkflow"
    nor may "werkflow" appear in their names without prior written
    permission of The Codehaus. "werkflow" is a registered
    trademark of The Codehaus.

 5. Due credit should be given to The Codehaus -
    http://werkflow.codehaus.org/

 THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import com.werken.werkflow.expr.ExpressionFactory;
import com.werken.werkflow.definition.petri.DefaultNet;
import com.werken.werkflow.definition.petri.DefaultArc;
import com.werken.werkflow.definition.petri.DefaultTransition;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

/** Create an input arc for a <code>Transition</code>.
 *
 *  <p>
 *  Used within a &lt;transition&gt; tag, the &lt;input&gt; tag
 *  is used to specify a &lt;place&gt; from which tokens are
 *  consumed.
 *  </p>
 *
 *  <p>
 *  <pre>
 *    &lt;place id="some.place"/&gt;
 *
 *    &lt;transition&gt;
 *      &lt;input from="some.place"/&gt;
 *    &lt;/transition&gt;
 *  </pre>
 *  </p>
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @see TransitionTag
 *  @see OutputTag
 *
 *  @version $Id$
 */
public class InputTag
    extends PetriTagSupport
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** From place identifier. */
    private String from;

    /** Filter expression. */
    private String filterExpr;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public InputTag()
    {
        // intentionally left blank
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Set the identifier of the <code>Place</code> from which tokens
     *  should be consumed.
     *
     *  @param from The place identifier.
     */
    public void setFrom(String from)
    {
        this.from = from;
    }

    /** Retrieve the identifier of the <code>Place</code> from which tokens
     *  should be consumed.
     *
     *  @return The place identifier.
     */
    public String getFrom()
    {
        return this.from;
    }

    /** Set the input filter expression.
     *
     *  @param filterExpr The filter expression.
     */
    public void setFilter(String filterExpr)
    {
        this.filterExpr = filterExpr;
    }

    /** Retrieve the input filter expression.
     *
     *  @return The filter expression.
     */
    public String getFilter()
    {
        return this.filterExpr;
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


        requireStringAttribute( "from",
                                getFrom() );

        DefaultArc arc = null;

        try
        {
            arc = net.connectPlaceToTransition( getFrom(),
                                                transition.getId() );

            if ( getFilter() != null )
            {
                arc.setExpression( newExpression( getFilter() ) );
            }

        }
        catch (Exception e)
        {
            throw new JellyTagException( e );
        }

        invokeBody( output );
    }
}
