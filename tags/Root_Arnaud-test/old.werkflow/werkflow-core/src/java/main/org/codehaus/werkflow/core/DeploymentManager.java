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

import org.codehaus.werkflow.ProcessInfo;
import org.codehaus.werkflow.NoSuchProcessException;
import org.codehaus.werkflow.admin.DeploymentException;
import org.codehaus.werkflow.admin.DuplicateProcessException;
import org.codehaus.werkflow.definition.ProcessDefinition;
import org.codehaus.werkflow.service.messaging.MessagingManager;
import org.codehaus.werkflow.service.persistence.PersistenceManager;
import org.codehaus.werkflow.service.persistence.ProcessPersistenceManager;

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
