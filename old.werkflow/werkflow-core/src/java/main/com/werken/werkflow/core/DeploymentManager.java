package com.werken.werkflow.core;

import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.NoSuchProcessException;
import com.werken.werkflow.admin.DeploymentException;
import com.werken.werkflow.admin.DuplicateProcessException;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.service.messaging.MessagingManager;
import com.werken.werkflow.service.persistence.PersistenceManager;
import com.werken.werkflow.service.persistence.ProcessPersistenceManager;
import com.werken.werkflow.service.persistence.PersistenceException;

import java.util.Map;
import java.util.HashMap;

class DeploymentManager
{
    private Map deployments;
    private Scheduler scheduler;
    private PersistenceManager persistenceManager;
    private MessagingManager messagingManager;

    DeploymentManager(Executor executor,
                      PersistenceManager persistenceManager,
                      MessagingManager messagingManager)
    {
        this.persistenceManager = persistenceManager;
        this.messagingManager   = messagingManager;

        this.scheduler   = new Scheduler( executor );
        this.deployments = new HashMap();
    }

    Scheduler getScheduler()
    {
        return this.scheduler;
    }

    PersistenceManager getPersistenceManager()
    {
        return this.persistenceManager;
    }

    MessagingManager getMessagingManager()
    {
        return this.messagingManager;
    }

    void deployProcess(ProcessDefinition processDef)
        throws DeploymentException
    {
        if ( this.deployments.containsKey( processDef.getId() ) )
        {
            throw new DuplicateProcessException( processDef );
        }

        ProcessPersistenceManager processPersist = getPersistenceManager().activate( processDef );

        ProcessDeployment deployment = new ProcessDeployment( processDef,
                                                              getScheduler(),
                                                              processPersist,
                                                              getMessagingManager() );

        try
        {
            deployment.initialize();

            this.deployments.put( new DeploymentKey( processDef.getPackageId(),
                                                     processDef.getId() ),
                                  deployment );
        }
        catch (Exception e)
        {
            throw new DeploymentException( processDef,
                                           e );
        }
    }

    ProcessDeployment getDeployment(String packageId,
                                    String processId)
        throws NoSuchProcessException
    {
        DeploymentKey key = new DeploymentKey( packageId,
                                               processId );

        if ( ! this.deployments.containsKey( key ) )
        {
            throw new NoSuchProcessException( packageId,
                                              processId );
        }

        return (ProcessDeployment) this.deployments.get( key );
    }

    ProcessInfo[] getProcesses()
    {
        return (ProcessInfo[]) this.deployments.values().toArray( ProcessInfo.EMPTY_ARRAY );
    }

    ProcessInfo getProcess(String packageId,
                           String processId)
        throws NoSuchProcessException
    {
        return getDeployment( packageId,
                              processId );
    }
}
