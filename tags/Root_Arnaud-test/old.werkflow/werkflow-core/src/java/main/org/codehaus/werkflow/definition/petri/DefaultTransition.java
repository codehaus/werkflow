package org.codehaus.werkflow.definition.petri;

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

import org.codehaus.werkflow.expr.Expression;
import org.codehaus.werkflow.definition.Waiter;
import org.codehaus.werkflow.task.Task;

/** Default <code>Transition</code> implementation.
 *
 *  @see DefaultNet#addTransition
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class DefaultTransition
    extends DefaultNode
    implements Transition
{
    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Activation expression, possibly null. */
    private Expression expression;

    /** Activation rule. */
    private ActivationRule activationRule;

    /** Waiter, possibly null. */
    private Waiter waiter;

    /** Task, possibly null. */
    private Task task;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param id The identifier.
     */
    public DefaultTransition(String id)
    {
        super( id );
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** @see Transition
     */
    public Arc[] getArcsFromPlaces()
    {
        return getInboundArcs();
    }

    /** @see Transition
     */
    public Arc[] getArcsToPlaces()
    {
        return getOutboundArcs();
    }

    /** Set the guarding <code>Expression</code>.
     *
     *  @see #getExpression
     *
     *  @param expression The expression.
     */
    public void setExpression(Expression expression)
    {
        this.expression = expression;
    }

    /** @see Transition
     */
    public Expression getExpression()
    {
        return this.expression;
    }

    /** Set the <code>ActivationRule</code>.
     *
     *  @see #getActivationRule
     *
     *  @param activationRule The activation rule.
     */
    public void setActivationRule(ActivationRule activationRule)
    {
        this.activationRule = activationRule;
    }

    /** @see Transition
     */
    public ActivationRule getActivationRule()
    {
        return this.activationRule;
    }

    /** Set the <code>Task</code>.
     *
     *  @see #getTask
     *
     *  @param task The task.
     */
    public void setTask(Task task)
    {
        this.task = task;
    }

    /** @see Transition
     */
    public Task getTask()
    {
        return this.task;
    }

    /** Set the <code>Waiter</code>.
     *
     *  @see #getWaiter
     *
     *  @param waiter The waiter.
     */
    public void setWaiter(Waiter waiter)
    {
        this.waiter = waiter;
    }

    /** @see Transition
     */
    public Waiter getWaiter()
    {
        return this.waiter;
    }

    public String toString()
    {
        return "[transition id=" + getId() + "]";
    }
}
