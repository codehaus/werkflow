package org.codehaus.werkflow.nonpersistent;

import org.codehaus.werkflow.DefaultInstance;
import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.Engine;

public class NonPersistentInstance
    extends DefaultInstance
{
    protected NonPersistentInstance(Engine engine,
                                    Workflow workflow,
                                    String id)
    {
        super( engine,
               workflow,
               id );
    }
}
