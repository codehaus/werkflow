package com.werken.werkflow.admin;

import com.werken.werkflow.WerkflowException;

public class AdminException
    extends WerkflowException
{
    public AdminException()
    {
    }

    public AdminException(Throwable rootCause)
    {
        super( rootCause );
    }
}
