package org.codehaus.werkflow.nonpersistent;

import org.codehaus.werkflow.*;

public class NonPersistentInstance
    extends RobustInstanceRef
{
    private DefaultInstance backup;

    protected NonPersistentInstance(DefaultInstance instance)
    {
        super( instance );
    }

    protected void startTransaction()
        throws Exception
    {
        if ( this.backup != null )
        {
            throw new AssumptionViolationError( "concurrent transactions not allowed" );
        }

        this.backup = (DefaultInstance) getInstance();
        setInstance( this.backup.duplicate() );
    }

    protected void commitTransaction()
        throws Exception
    {
        this.backup = null;
    }

    protected void abortTransaction()
        throws Exception
    {
        setInstance( this.backup );
        this.backup = null;
    }
}
