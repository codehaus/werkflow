package org.codehaus.werkflow.simple;

import org.codehaus.werkflow.spi.Instance;
import org.codehaus.werkflow.spi.SyncComponent;

import java.util.Properties;

class Action
    implements SyncComponent
{
    private ActionManager manager;
    private String id;
    private Properties properties;

    Action(ActionManager manager,
           String id,
           Properties properties)
    {
        this.manager    = manager;
        this.id         = id;
        this.properties = properties;
    }

    public void perform(Instance instance)
        throws Exception
    {
        this.manager.perform( this.id,
                              instance,
                              this.properties );
    }
}
