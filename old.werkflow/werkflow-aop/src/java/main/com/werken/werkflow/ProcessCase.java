package com.werken.werkflow;

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

/** Runtime handle for an instance of a process.
 *
 *  <p>
 *  The <code>ProcessCase</code> interface provides a read-only view
 *  of a running process instance.  Any mutation activities are performed
 *  within the realm of an {@link com.werken.werkflow.action.Action}.
 *  </p>
 *
 *  @see com.werken.werkflow.action.Action
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public interface ProcessCase
    extends Attributes
{
    /** Retrieve the identifier for this case.
     *
     *  <p>
     *  The identifier for the case is universally unique
     *  amongst all instances of all processes within a system.
     *  </p>
     *
     *  @return The unique identifier.
     */
    String getId();

    /** Retrieve the <code>ProcessInfo</code>.
     *
     *  <p>
     *  The <code>ProcessInfo</code> provides meta-information
     *  regarding the process of which this is an instance.
     *  </p>
     *
     *  @return THe process-info.
     */
    ProcessInfo getProcessInfo();
}
