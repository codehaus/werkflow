package org.codehaus.werkflow.nonpersistent;

import org.codehaus.werkflow.*;

public class NonPersistentInstance
    extends RobustInstanceRef
{
    private DefaultInstance backup;

    NonPersistentInstance(DefaultInstance instance)
    {
        super( instance );
    }

    void startTransaction()
        throws Exception
    {
        if ( this.backup != null )
        {
            throw new AssumptionViolationError( "concurrent transactions not allowed" );
        }

        this.backup = (DefaultInstance) getInstance();
        setInstance( this.backup.duplicate() );
    }

    void commitTransaction()
        throws Exception
    {
        this.backup = null;
    }

    void abortTransaction()
        throws Exception
    {
        setInstance( this.backup );
        this.backup = null;
    }
    
}
