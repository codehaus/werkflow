package com.werken.werkflow.definition;

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

import com.werken.werkflow.work.Action;

/** Annotation to a <code>ProcessDefinition</code> specifying
 *  how a message may initiate a new <code>ProcessCase</code>.
 *
 *  <p>
 *  When a message of the <code>MessageType</code> is received,
 *  the associated <code>Action</code> is executed with the message
 *  in <code>otherAttrs</code>.  The purpose of the <code>Action</code>
 *  is to initate the <code>ProcessCase<code>'s own attributes,
 *  as the message is <b>not</b> by default saved in the case
 *  attributes.
 *  </p>
 *
 *  @see ProcessDefinition
 *  @see MessageType
 *  @see Action
 *  @see com.werken.werkflow.ProcessCase
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class MessageInitiator
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    /** Empty <code>MessageInitiator</code> array. */
    public static final MessageInitiator[] EMPTY_ARRAY = new MessageInitiator[0];

    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Initiating message-type. */
    private MessageType messageType;

    /** Other-attributes binding variable name. */
    private String bindingVar;

    /** Documentation, possibly null. */
    private String documentation;

    /** Initialization action, possibly null. */
    private Action action;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param messageType The initiating message-type.
     *  @param bindingVar The other-attributes binding variable name.
     */
    public MessageInitiator(MessageType messageType,
                            String bindingVar)
    {
        this.messageType = messageType;
        this.bindingVar  = bindingVar;
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Set the documentation.
     *
     *  @param documentation The documentation.
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

    /** Retrieve the initiating <code>MessageType</code>.
     *
     *  @return The messagektype.
     */
    public MessageType getMessageType()
    {
        return this.messageType;
    }

    /** Retrieve the other-attributes binding variable name.
     *
     *  @return The binding variable name.
     */
    public String getBindingVar()
    {
        return this.bindingVar;
    }

    /** Set the initialization <code>Action</code>.
     *
     *  @param action The initialization action.
     */
    public void setAction(Action action)
    {
        this.action = action;
    }

    /** Retrieve the initialization <code>Action</code>.
     *
     *  @return The initialization action.
     */
    public Action getAction()
    {
        return this.action;
    }
}
    
