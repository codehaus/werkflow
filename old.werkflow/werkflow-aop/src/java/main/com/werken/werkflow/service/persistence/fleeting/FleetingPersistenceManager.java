package com.werken.werkflow.service.persistence.fleeting;

import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.admin.DeploymentException;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.service.persistence.PersistenceManager;
import com.werken.werkflow.service.persistence.ProcessPersistenceManager;
import com.werken.werkflow.service.persistence.PersistenceException;

public class FleetingPersistenceManager
    implements PersistenceManager
{
    public FleetingPersistenceManager()
    {

    }

    public ProcessPersistenceManager deployProcess(ProcessInfo processDef)
        throws DeploymentException
    {
        return new FleetingProcessPersistenceManager( processDef.getPackageId(),
                                                      processDef.getId() );
    }
}
