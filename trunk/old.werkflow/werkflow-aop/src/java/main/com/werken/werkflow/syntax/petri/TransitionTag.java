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

import com.werken.werkflow.definition.Waiter;
import com.werken.werkflow.definition.petri.DefaultNet;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.AndInputRule;
import com.werken.werkflow.definition.petri.OrInputRule;
import com.werken.werkflow.definition.petri.DuplicateIdException;
import com.werken.werkflow.task.Task;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

/** Define a <code>Transition</code> in the Petri net structure.
 *
 *  <p>
 *  The &lt;transition&gt; must contain a <code>id</code> attribute
 *  and may contain an optional &lt;documentation&gt; element.
 *  At least one each of &lt;input&gt; and &lt;output&gt; tags are
 *  required.  A &lt;task&gt; is optional.  For message-triggered
 *  transitions, a &lt;message&gt; element specifies the message information.
 *  </p>
 *
 *  <p>
 *  <pre>
 *  &lt;transition id="some.transition"&gt;
 *    &lt;documentation&gt;
 *      This is my transition
 *    &lt;/documentation&gt;
 *    &lt;input from="in"/&gt;
 *    &lt;output  to="out"/&gt;
 *    &lt;message type="SomeMessageType" id="theMsg"/&gt;
 *    &lt;task&gt;
 *      ...
 *    &lt;/task&gt;
 *  &lt;transition&gt;
 *  </pre>
 *  </p>
 *
 *  <p>
 *  The optional <code>type</code> attribute can be used to change
 *  the way the &lt;input&gt;s are evaluated.  It defaults to the
 *  value <b>and</b> and may optionally be set to <b>or</b>.  By default,
 *  each &lt;input&gt; must contain a token before the transition is
 *  considered to be ready to fire.  If the type is <b>or</b>, then any
 *  single &lt;input&gt; containing a token is sufficient to enabled
 *  the transition for firing.
 *  </p>
 *
 *  @see DocumentationTag
 *  @see InputTag
 *  @see OutputTag
 *  @see MessageTag
 *  @see TaskTag
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class TransitionTag
    extends PetriTagSupport
      // implements DocumentableTag
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Identifier. */
    private String id;

    /** Type {"and", "or"} */
    private String type;

    /** Documentation. */
    private String documentation;

    /** Readiness guard expression. */
    private String guardExpr;

    /** Task, possibly null. */
    private Task task;

    private Waiter waiter;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public TransitionTag()
    {
        // intentionally left blank.
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Set the identifier.
     *
     *  @param id The identifier.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /** Retrieve the identifier.
     *
     *  @return The identifier.
     */
    public String getId()
    {
        return this.id;
    }

    /** @see DocumentableTag
     */
    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    /** Retrieve the documentation.
     *
     *  @return The documentation.
     */
    public String getDocumentation()
    {
        return this.documentation;
    }

    /** Set the readiness guard expression.
     *
     *  @param guardExpr The expression.
     */
    public void setGuard(String guardExpr)
    {
        this.guardExpr = guardExpr;
    }

    /** Retrieve the readiness guard expression.
     *
     *  @return The expression.
     */
    public String getGuard()
    {
        return this.guardExpr;
    }

    /** Set the Transition type.
     *
     *  @param type Either the string "and" or the string "or".
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /** Retrieve the transition type.
     *
     *  @return The transition type.
     */
    public String getType()
    {
        return this.type;
    }

    /** Set the <code>Task</code>.
     *
     *  @param task The task.
     */
    public void setTask(Task task)
    {
        this.task = task;
    }

    /** Retrieve the <code>Task</code>.
     *
     *  @return The task.
     */
    public Task getTask()
    {
        return this.task;
    }

    /** Set the <code>Waiter</code>.
     *
     *  @param waiter The waiter.
     */
    public void setWaiter(Waiter waiter)
    {
        this.waiter = waiter;
    }

    /** Retrieve the <code>Waiter</code>.
     *
     *  @return The waiter.
     */
    public Waiter getWaiter()
    {
        return this.waiter;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** @see org.apache.commons.jelly.Tag
     */
    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        DefaultNet net = getCurrentNet();

        requireStringAttribute( "id",
                                getId() );
        
        DefaultTransition transition = null;

        try
        {
            transition = net.addTransition( getId() );
        }
        catch (DuplicateIdException e)
        {
            throw new JellyTagException( e );
        }
        
        if ( getType() == null
             ||
             getType().equals( "and" ) )
        {
            transition.setActivationRule( AndInputRule.getInstance() );
        }
        else if ( getType().equals( "or" ) )
        {
            transition.setActivationRule( OrInputRule.getInstance() );
        }
        else
        {
            throw new JellyTagException( "invalid 'type' attribute; must be one of: 'and' 'or'" );
        }
        
        setDocumentation( null );

        getContext().setVariable( DefaultTransition.class.getName(),
                                  transition );

        setCurrentTransition( transition );
        invokeBody( output );
        setCurrentTransition( null );

        getContext().removeVariable( DefaultTransition.class.getName() );

        transition.setDocumentation( getDocumentation() );

        try
        {
            if ( getGuard() != null )
            {
                transition.setExpression( newExpression( getGuard() ) );
            }
        }
        catch (Exception e)
        {
            throw new JellyTagException( e );
        }

        if ( getTask() != null )
        {
            transition.setTask( getTask() );
        }

        if ( getWaiter() != null )
        {
            transition.setWaiter( getWaiter() );
        }

        setGuard( null );
        setTask( null );
    }
}
