package org.codehaus.werkflow.core;

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

import org.codehaus.werkflow.Attributes;
import org.codehaus.werkflow.ProcessInfo;
import org.codehaus.werkflow.NoSuchCaseException;
import org.codehaus.werkflow.service.persistence.ProcessPersistenceManager;
import org.codehaus.werkflow.service.persistence.CaseTransfer;
import org.codehaus.werkflow.service.persistence.PersistenceException;

import java.util.Map;
import java.util.HashMap;

class CaseManager
{
    private ChangeSetSource changeSetSource;
    private CaseEvaluator caseEvaluator;
    private MessageConsumer messageConsumer;
    private ProcessInfo processInfo;
    private ProcessPersistenceManager persistenceManager;

    private Map cases;

    CaseManager(ChangeSetSource changeSetSource,
                CaseEvaluator caseEvaluator,
                MessageConsumer messageConsumer,
                ProcessInfo processInfo,
                ProcessPersistenceManager persistenceManager)
    {
        this.changeSetSource    = changeSetSource;
        this.caseEvaluator      = caseEvaluator;
        this.messageConsumer    = messageConsumer;
        this.processInfo        = processInfo;
        this.persistenceManager = persistenceManager;

        this.cases           = new HashMap();
    }

    private ChangeSetSource getChangeSetSource()
    {
        return this.changeSetSource;
    }

    private CaseEvaluator getCaseEvaluator()
    {
        return this.caseEvaluator;
    }

    private MessageConsumer getMessageConsumer()
    {
        return this.messageConsumer;
    }

    private ProcessInfo getProcessInfo()
    {
        return this.processInfo;
    }

    private ProcessPersistenceManager getPersistenceManager()
    {
        return this.persistenceManager;
    }

    synchronized CoreProcessCase getCase(String caseId)
        throws NoSuchCaseException, PersistenceException
    {
        if ( ! this.cases.containsKey( caseId ) )
        {
            if ( ! getPersistenceManager().hasCase( caseId ) )
            {
                throw new NoSuchCaseException( getProcessInfo().getPackageId(),
                                               getProcessInfo().getId(),
                                               caseId );
            }

            addCase( new CoreProcessCase( getPersistenceManager(),
                                          getChangeSetSource(),
                                          getCaseEvaluator(),
                                          getMessageConsumer(),
                                          getProcessInfo(),
                                          caseId ) );
        }

        return (CoreProcessCase) this.cases.get( caseId );
    }

    synchronized CoreProcessCase newCase(Attributes attributes)
        throws PersistenceException
    {
        CaseTransfer caseTransfer = getPersistenceManager().newCase( attributes );

        CoreProcessCase processCase = new CoreProcessCase( getPersistenceManager(),
                                                           getChangeSetSource(),
                                                           getCaseEvaluator(),
                                                           getMessageConsumer(),
                                                           getProcessInfo(),
                                                           caseTransfer );

        addCase( processCase );

        return processCase;
    }

    private void addCase(CoreProcessCase processCase)
    {
        this.cases.put( processCase.getId(),
                        processCase );
    }
}
