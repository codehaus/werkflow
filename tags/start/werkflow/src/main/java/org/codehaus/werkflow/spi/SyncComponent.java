package org.codehaus.werkflow.spi;

public interface SyncComponent
    extends Component
{
    void perform(Instance instance)
        throws Exception;
}
