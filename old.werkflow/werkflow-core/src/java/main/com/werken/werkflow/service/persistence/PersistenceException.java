package com.werken.werkflow.service.persistence;

import com.werken.werkflow.WerkflowException;

public class PersistenceException
    extends WerkflowException
{
    public PersistenceException(String message)
    {
        super( message );
    }

    public PersistenceException(Throwable rootCause)
    {
        super( rootCause );
    }
}
