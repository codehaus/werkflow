package com.werken.werkflow.service.persistence;

import com.werken.werkflow.admin.DeploymentException;
import com.werken.werkflow.definition.ProcessDefinition;

public interface PersistenceManager
{
     ProcessPersistenceManager deployProcess(ProcessDefinition processDefinition)
         throws DeploymentException;
}
