package com.werken.werkflow.engine;

import com.werken.werkflow.WerkflowException;

public class EngineException
    extends WerkflowException
{
    public EngineException()
    {
    }

    public EngineException(Throwable rootCause)
    {
        super( rootCause );
    }
}
