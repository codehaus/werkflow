package com.werken.werkflow.service;

import com.werken.werkflow.service.caserepo.CaseRepository;
import com.werken.werkflow.service.messaging.MessagingManager;

public class SimpleWfmsServices
    implements WfmsServices
{
    private CaseRepository caseRepo;
    private MessagingManager messagingManager;

    public SimpleWfmsServices()
    {
    }

    public void setCaseRepository(CaseRepository caseRepo)
    {
        this.caseRepo = caseRepo;
    }

    public CaseRepository getCaseRepository()
    {
        return this.caseRepo;
    }

    public void setMessagingManager(MessagingManager messagingManager)
    {
        this.messagingManager = messagingManager;
    }

    public MessagingManager getMessagingManager()
    {
        return this.messagingManager;
    }
}
