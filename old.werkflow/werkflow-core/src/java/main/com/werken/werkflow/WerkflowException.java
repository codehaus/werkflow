package com.werken.werkflow;

public class WerkflowException
    extends Exception
{
    private Throwable rootCause;

    public WerkflowException()
    {
    }

    public WerkflowException(Throwable rootCause)
    {
        this.rootCause = rootCause;
    }

    public Throwable getRootCause()
    {
        return this.rootCause;
    }
}

