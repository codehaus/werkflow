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

import org.codehaus.werkflow.definition.Waiter;
import org.codehaus.werkflow.expr.Expression;
import org.codehaus.werkflow.task.Task;

/** Transition within a Petri net.
 *
 *  <p>
 *  In Petri nets, transitions define active points that move
 *  tokens from one place to another while performing some
 *  activity.
 *  </p>
 *
 *  @see Place
 *  @see Net
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public interface Transition
    extends Node
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    /** Empty <code>Transition</code> array. */
    static final Transition[] EMPTY_ARRAY = new Transition[0];

    // ----------------------------------------------------------------------
    //     Interface
    // ----------------------------------------------------------------------

    /** Retrieve inbound <code>Arc</code>s from <code>Place</code>s.
     *
     *  @return The inbound arcs.
     */
    Arc[] getArcsFromPlaces();

    /** Retrieve outbound <code>Arc</code>s towards <code>Place</code>s.
     *
     *  @return The outbound arcs.
     */
    Arc[] getArcsToPlaces();


    /** Retrieve guarding <code>Expression</code> if any.
     *
     *  @todo Adjust to actually use the expr.
     *
     *  @return The guarding expression or <code>null</code> if none.
     */
    Expression getExpression();

    /** Retrieve the <code>ActivationRule</code>.
     *
     *  <p>
     *  The <code>ActivationRule</code> determines the types
     *  of conditions that must be met for the transition to
     *  be activated, or capable of firing.
     *  </p>
     *
     *  @see AndInputRule
     *  @see OrInputRule
     *
     *  @return The activation rule.
     */
    ActivationRule getActivationRule();

    /** Retrieve the <code>Task</code>, if any.
     *
     *  <p>
     *  The <code>Task</code> is the unit of work that
     *  is triggered when this transition is fired.
     *  </p>
     *
     *  @return The task, or <code>null</code> if none.
     */
    Task getTask();

    Waiter getWaiter();
}
