package org.codehaus.werkflow;

public interface SyncComponent
    extends Component
{
    void perform(Instance instance)
        throws Exception;
}
