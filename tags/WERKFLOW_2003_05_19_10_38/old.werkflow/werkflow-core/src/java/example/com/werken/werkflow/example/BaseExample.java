package com.werken.werkflow.example;

import com.werken.werkflow.Wfms;
import com.werken.werkflow.engine.WorkflowEngine;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.service.SimpleWfmsServices;
import com.werken.werkflow.service.messaging.simple.SimpleMessagingManager;
import com.werken.werkflow.service.caserepo.CaseRepository;
import com.werken.werkflow.service.caserepo.InMemoryCaseRepository;
import com.werken.werkflow.personality.basic.BasicPersonality;

import java.net.URL;

public class BaseExample
{
    private Wfms wfms;
    private SimpleMessagingManager messagingManager;
    private CaseRepository caseRepository;

    public BaseExample()
    {

    }

    public Wfms getWfms()
    {
        if ( this.wfms == null )
        {
            SimpleWfmsServices services = new SimpleWfmsServices();
            
            services.setMessagingManager( getMessagingManager() );
            services.setCaseRepository( getCaseRepository() );
            
            this.wfms = new WorkflowEngine( services );
        }

        return this.wfms;
    }

    public SimpleMessagingManager getMessagingManager()
    {
        if ( this.messagingManager == null )
        {
            this.messagingManager = new SimpleMessagingManager();
        }

        return this.messagingManager;
    }

    public CaseRepository getCaseRepository()
    {
        if ( this.caseRepository == null )
        {
            this.caseRepository = new InMemoryCaseRepository();
        }

        return this.caseRepository;
    }

    public void deploy(String name)
        throws Exception
    {
        BasicPersonality personality = BasicPersonality.getInstance();

        URL url = getClass().getResource( name );

        ProcessDefinition[] processDefs = personality.load( url );

        for ( int i = 0 ; i < processDefs.length ; ++i )
        {
            getWfms().getAdmin().deployProcess( processDefs[i] );
        }
    }
}
