package org.codehaus.werkflow;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

class Locker
{
    private Map locks;

    Locker()
    {
        this.locks = new HashMap();
    }

    public synchronized void acquireLock(String transactionId,
                                         String lockId)
        throws InterruptedException
    {
        while ( this.locks.containsKey( lockId )
                &&
                ! this.locks.get( lockId ).equals( transactionId ) )
        {
            wait();
        }

        this.locks.put( lockId,
                        transactionId );
    }

    public synchronized void dropLock(String transactionId,
                                      String lockId)
    {
        if ( ! this.locks.containsKey( lockId ) )
        {
            throw new AssumptionViolationError( "lock [" + lockId + "] help by no transaction" );
        }

        if ( ! this.locks.get( lockId ).equals( transactionId ) )
        {
            throw new AssumptionViolationError( "lock [" + lockId + "] help by different transaction" );
        }

        this.locks.remove( lockId );

        notifyAll();
    }
}
