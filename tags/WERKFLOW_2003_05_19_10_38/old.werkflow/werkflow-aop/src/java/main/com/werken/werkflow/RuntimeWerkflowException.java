package com.werken.werkflow;

public class RuntimeWerkflowException
    extends RuntimeException
{
    private Throwable rootCause;

    public RuntimeWerkflowException(Throwable rootCause)
    {
        this.rootCause = rootCause;
    }

    public Throwable getRootCause()
    {
        return this.rootCause;
    }
}
