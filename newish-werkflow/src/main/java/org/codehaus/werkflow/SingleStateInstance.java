package org.codehaus.werkflow;

import org.codehaus.werkflow.spi.InstanceRef;

public class SingleStateInstance
    extends InstanceRef
{
    public SingleStateInstance(Instance instance)
    {
        super( instance );
    }

    public String getState()
    {
        return (String) get( "org.codehaus.werkflow.SingleStateInstance|state" );
    }
}
