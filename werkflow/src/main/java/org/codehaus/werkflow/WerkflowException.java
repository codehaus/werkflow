package org.codehaus.werkflow;

public class WerkflowException
    extends Exception
{
    public WerkflowException()
    {
    }

    public WerkflowException(Throwable rootCause)
    {
        super( rootCause );
    }
}
