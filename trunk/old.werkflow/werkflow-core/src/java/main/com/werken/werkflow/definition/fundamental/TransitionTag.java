package com.werken.werkflow.definition.fundamental;

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

import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.definition.petri.DefaultNet;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.AndInputRule;
import com.werken.werkflow.definition.petri.OrInputRule;
import com.werken.werkflow.definition.petri.DuplicateIdException;
import com.werken.werkflow.semantics.jelly.JellyExpression;
import com.werken.werkflow.task.Task;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.expression.Expression;

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
    extends FundamentalTagSupport
    implements DocumentableTag
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

    /** Readiness test expression. */
    private Expression expression;

    /** Task, possibly null. */
    private Task task;

    /** Message-waiter corresponding to message for firing, possibly null. */
    private MessageWaiter messageWaiter;

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

    /** Set the readiness test expression.
     *
     *  @param expression The expression.
     */
    public void setTest(Expression expression)
    {
        this.expression = expression;
    }

    /** Retrieve the readiness test expression.
     *
     *  @return The expression.
     */
    public Expression getTest()
    {
        return this.expression;
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

    /** Set the <code>MessageWaiter</code>.
     *
     *  @param messageWaiter The message-waiter.
     */
    public void setMessageWaiter(MessageWaiter messageWaiter)
    {
        this.messageWaiter = messageWaiter;
    }

    /** Retrieve the <code>MessageWaiter</code>.
     *
     *  @return The message-waiter.
     */
    public MessageWaiter getMessageWaiter()
    {
        return this.messageWaiter;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** @see org.apache.commons.jelly.Tag
     */
    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        ProcessTag process = (ProcessTag) requiredAncestor( "process",
                                                            ProcessTag.class );
        
        requireStringAttribute( "id",
                                getId() );
        
        DefaultNet net = process.getNet();
        
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

        invokeBody( output );

        transition.setDocumentation( getDocumentation() );

        if ( getTest() != null )
        {
            transition.setExpression( new JellyExpression( getTest() ) );
        }

        if ( getTask() != null )
        {
            transition.setTask( getTask() );
        }

        if ( getMessageWaiter() != null )
        {
            transition.setMessageWaiter( getMessageWaiter() );
        }

        this.expression = null;
        this.task       = null;
    }
}
