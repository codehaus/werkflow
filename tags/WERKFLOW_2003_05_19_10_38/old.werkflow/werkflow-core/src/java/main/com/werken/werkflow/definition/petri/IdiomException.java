package com.werken.werkflow.definition.petri;

import com.werken.werkflow.WerkflowException;

public class IdiomException
    extends WerkflowException
{
    public IdiomException()
    {
    }

    public IdiomException(Throwable rootCause)
    {
        super( rootCause );
    }
}
