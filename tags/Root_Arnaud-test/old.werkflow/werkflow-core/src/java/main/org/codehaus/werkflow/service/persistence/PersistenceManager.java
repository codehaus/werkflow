package org.codehaus.werkflow.service.persistence;

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

import org.codehaus.werkflow.ProcessInfo;
import org.codehaus.werkflow.admin.DeploymentException;

public interface PersistenceManager
{
    /** Component role. */
    public static final String ROLE = PersistenceManager.class.getName();

    /** Activate the {@link ProcessPersistenceManager} that will maintain
     * the {@link org.codehaus.werkflow.core.CaseState} information for the process.
     *
     * <p>
     * This method may be called for new processes and processes that have been activated before.
     * </p>
     *
     * <p>
     * A call to {@link #passivate} must be made before another call to activate is made
     * for the same process. Implementations should throw a {@link DeploymentException}
     * if this rule is violated.
     * </p>
     *
     * @param processInfo The process to activate
     *
     * @return The ProcessPersistenceManager instance the engine should use to store case details.
     *
     * @throws DeploymentException if the PersistenceManager instance is not able to activate
     *     the process or the process is already active
     *
     * @see ProcessPersistenceManager
     * @see org.codehaus.werkflow.engine.WorkflowEngine
     */
    ProcessPersistenceManager activate(ProcessInfo processInfo) throws DeploymentException;


    /** The engine is no longer using the supplied {@link ProcessPersistenceManager}.
     *
     * <p>
     * The PersistenceManager is free to release any resources associated with the
     * {@link ProcessPersistenceManager}.
     * </p>
     *
     * <p>
     * If the supplied manager is not active or unknown to the PersistenceManager instance
     * then a {@link PersistenceException} will be thrown.
     * </p>
     *
     * @param manager The ProcessPersistenceManager to passivate
     *
     * @throws PersistenceException If the ProcessPersistenceManager is not active or unknown to the
     *     PersistenceManager.
     *
     * @see ProcessPersistenceManager
     * @see org.codehaus.werkflow.engine.WorkflowEngine
     */
    void passivate(ProcessPersistenceManager manager) throws PersistenceException;
}
