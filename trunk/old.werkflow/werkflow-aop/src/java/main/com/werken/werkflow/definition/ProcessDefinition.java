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

import com.werken.werkflow.definition.petri.Net;

import com.werken.werkflow.ProcessInfo;

/** Definition of a process.
 *
 *  <p>
 *  A process is defined using a Petri <code>Net</code>.
 *  Additionally, <code>MessageInitiator</code>s may be
 *  attached to allow initiation to occur as the result
 *  of the receipt of a message.
 *  </p>
 *
 *  @see MessageInitiator
 *  @see Net
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class ProcessDefinition
    implements ProcessInfo
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    /** Empty <code>ProcessDefinition</code> array. */
    public static final ProcessDefinition[] EMPTY_ARRAY = new ProcessDefinition[0];

    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Process identifier. */
    private String id;

    /** Documentation, possibly null. */
    private String documentation;

    /** Petri-net structure. */
    private Net net;

    /** Message initiators, possibly empty. */
    private MessageInitiator[] messageInitiators;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param id The unique process identifier.
     *  @param net The Petri net structure.
     *  @param messageInitiators The message initiators.
     */
    public ProcessDefinition(String id,
                             Net net,
                             MessageInitiator[] messageInitiators)
    {
        this.id  = id;
        this.net = net;

        this.messageInitiators = messageInitiators;
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** @see ProcessInfo
     */
    public String getId()
    {
        return this.id;
    }

    /** Set the documentation.
     *
     *  @param documentation The documentation.
     */
    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    /** @see ProcessInfo
     */
    public String getDocumentation()
    {
        return this.documentation;
    }
    
    /** Retrieve the Petri <code>Net</code> structure.
     *
     *  @return The Petri net.
     */
    public Net getNet()
    {
        return this.net;
    }

    /** Retrieve the <code>MessageInitiator</code>s.
     *
     *  @return The possibly empty message-initiator array.
     */
    public MessageInitiator[] getMessageInitiators()
    {
        return this.messageInitiators;
    }
}
