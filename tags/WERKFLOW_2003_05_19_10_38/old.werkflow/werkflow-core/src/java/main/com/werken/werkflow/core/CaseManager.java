package com.werken.werkflow.core;

import com.werken.werkflow.Attributes;
import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.NoSuchCaseException;
import com.werken.werkflow.service.persistence.ProcessPersistenceManager;
import com.werken.werkflow.service.persistence.CaseTransfer;
import com.werken.werkflow.service.persistence.PersistenceException;

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
