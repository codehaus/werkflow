package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.Instance;

public interface SyncComponent
    extends Component
{
    void perform(Instance instance)
        throws Exception;
}
