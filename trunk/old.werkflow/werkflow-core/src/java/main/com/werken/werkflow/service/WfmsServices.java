package com.werken.werkflow.service;

import com.werken.werkflow.service.caserepo.CaseRepository;
import com.werken.werkflow.service.messaging.MessagingManager;

public interface WfmsServices
{
    CaseRepository getCaseRepository();
    MessagingManager getMessagingManager();
}
