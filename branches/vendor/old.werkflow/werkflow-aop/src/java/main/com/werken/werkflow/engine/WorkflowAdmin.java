package com.werken.werkflow.engine;

import com.werken.werkflow.admin.WfmsAdmin;
import com.werken.werkflow.admin.ProcessException;
import com.werken.werkflow.definition.ProcessDefinition;

public class WorkflowAdmin
    implements WfmsAdmin
{
    private WorkflowEngine engine;

    public WorkflowAdmin(WorkflowEngine engine)
    {
        this.engine = engine;
    }

    public WorkflowEngine getEngine()
    {
        return this.engine;
    }

    public void deployProcess(ProcessDefinition processDef)
        throws ProcessException
    {
        getEngine().deployProcess( processDef );
    }
                              
}
