package com.werken.werkflow.service.persistence;

import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.admin.DeploymentException;
import com.werken.werkflow.definition.ProcessDefinition;

public interface PersistenceManager
{
     ProcessPersistenceManager deployProcess(ProcessInfo processInfo)
         throws DeploymentException;
}
