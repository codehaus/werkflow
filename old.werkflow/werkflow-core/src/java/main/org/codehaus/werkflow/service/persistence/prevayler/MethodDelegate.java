package org.codehaus.werkflow.service.persistence.prevayler;

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

import java.io.Serializable;

import org.codehaus.werkflow.Attributes;
import org.codehaus.werkflow.service.persistence.CaseTransfer;
import org.codehaus.werkflow.service.persistence.ChangeSet;
import org.codehaus.werkflow.service.persistence.CorrelationTransfer;
import org.codehaus.werkflow.service.persistence.PersistenceException;

/**
 * @author kevin
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
interface MethodDelegate extends Serializable
{
    /**
     * @see org.codehaus.werkflow.service.persistence.ProcessPersistenceManager#persist(org.codehaus.werkflow.service.persistence.ChangeSet)
     */
    void persist(ChangeSet changeSet) throws PersistenceException;
    /**
     * @see org.codehaus.werkflow.service.persistence.ProcessPersistenceManager#hasCase(java.lang.String)
     */
    boolean hasCase(String caseId);
    /**
     * @see org.codehaus.werkflow.service.persistence.ProcessPersistenceManager#newCase(org.codehaus.werkflow.Attributes)
     */
    CaseTransfer newCase(Attributes initialiAttrs) throws PersistenceException;
    /**
     * @see org.codehaus.werkflow.service.persistence.ProcessPersistenceManager#loadCase(java.lang.String)
     */
    CaseTransfer loadCase(String caseId) throws PersistenceException;
    /**
     * @see org.codehaus.werkflow.service.persistence.ProcessPersistenceManager#getCorrelations()
     */
    CorrelationTransfer[] getCorrelations() throws PersistenceException;
}
