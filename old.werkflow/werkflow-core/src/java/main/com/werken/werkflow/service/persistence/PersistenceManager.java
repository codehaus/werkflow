package com.werken.werkflow.service.persistence;

import com.werken.werkflow.definition.ProcessDefinition;

public interface PersistenceManager
{
     ProcessPersistenceManager deployProcess(ProcessDefinition processDefinition);
}
