package org.codehaus.werkflow.nonpersistent;

import org.codehaus.werkflow.*;
import org.codehaus.werkflow.spi.*;

public class NonPersistentInstance
    extends RobustInstanceRef
{
    private DefaultInstance backup;

    public NonPersistentInstance(DefaultInstance instance)
    {
        super( instance );
    }

    public void startTransaction()
        throws Exception
    {
        if ( this.backup != null )
        {
            throw new AssumptionViolationError( "concurrent transactions not allowed" );
        }

        this.backup = (DefaultInstance) getInstance();
        setInstance( this.backup.duplicate() );
    }

    public void commitTransaction()
        throws Exception
    {
        this.backup = null;
    }

    public void abortTransaction()
        throws Exception
    {
        setInstance( this.backup );
        this.backup = null;
    }
}
