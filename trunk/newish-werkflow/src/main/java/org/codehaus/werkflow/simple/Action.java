package org.codehaus.werkflow.simple;

import org.codehaus.werkflow.spi.SyncComponent;
import org.codehaus.werkflow.Instance;

class Action
    implements SyncComponent
{
    private ActionManager manager;
    private String id;

    Action(ActionManager manager,
           String id)
    {
        this.manager = manager;
        this.id      = id;
    }

    public void perform(Instance instance)
        throws Exception
    {
        this.manager.perform( this.id,
                              instance );
    }
}
