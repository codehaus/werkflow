package com.werken.werkflow.service.persistence.fleeting;

import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.admin.DeploymentException;
import com.werken.werkflow.service.persistence.PersistenceException;
import com.werken.werkflow.service.persistence.PersistenceManager;
import com.werken.werkflow.service.persistence.ProcessPersistenceManager;

public class FleetingPersistenceManager
    implements PersistenceManager
{
    public FleetingPersistenceManager()
    {

    }

    public ProcessPersistenceManager activate(ProcessInfo processDef)
        throws DeploymentException
    {
        return new FleetingProcessPersistenceManager( processDef.getPackageId(),
                                                      processDef.getId() );
    }

    /**
     * @see com.werken.werkflow.service.persistence.PersistenceManager#passivate(com.werken.werkflow.service.persistence.ProcessPersistenceManager)
     */
    public void passivate(ProcessPersistenceManager manager) throws PersistenceException
    {
        ; // move along .... nothing to see here
    }
}
