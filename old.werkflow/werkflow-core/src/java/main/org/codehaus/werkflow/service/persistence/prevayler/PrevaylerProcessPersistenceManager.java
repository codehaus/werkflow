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

import org.prevayler.Prevayler;

import org.codehaus.werkflow.Attributes;
import org.codehaus.werkflow.service.persistence.CaseTransfer;
import org.codehaus.werkflow.service.persistence.ChangeSet;
import org.codehaus.werkflow.service.persistence.CorrelationTransfer;
import org.codehaus.werkflow.service.persistence.PersistenceException;
import org.codehaus.werkflow.service.persistence.ProcessPersistenceManager;

class PrevaylerProcessPersistenceManager implements ProcessPersistenceManager
{
    public PrevaylerProcessPersistenceManager(Prevayler prevayler, ProcessState state)
    {
        if (null == state)
        {
            throw new IllegalArgumentException("The supplied ProcessState can not be null.");
        }

        _key = state.key();

        // new process managers are always active
        _delegate = new ActiveDelegate(prevayler, state);
    }

    private MethodDelegate _delegate;
    private ManagerKey _key;

    // -- ProcessPersistenceManager implementation

    public void persist(ChangeSet changeSet) throws PersistenceException
    {
        _delegate.persist(changeSet);
    }

    public boolean hasCase(String caseId)
    {
        return _delegate.hasCase(caseId);
    }

    public CaseTransfer newCase(Attributes initialiAttrs) throws PersistenceException
    {
        return _delegate.newCase(initialiAttrs);
    }

    public CaseTransfer loadCase(String caseId) throws PersistenceException
    {
        return _delegate.loadCase(caseId);
    }

    public CorrelationTransfer[] getCorrelations() throws PersistenceException
    {
        return _delegate.getCorrelations();
    }

    // -- for my "friends"

    ManagerKey key()
    {
        return _key;
    }

    void passivate()
    {
        _delegate = new PassiveDelegate();
    }
}
