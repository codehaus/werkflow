package com.werken.werkflow;

public class QueryException
    extends WerkflowException
{
    public QueryException(Throwable rootCause)
    {
        super( rootCause );
    }

    public String getMessage()
    {
        String msg = "error performing query";

        if ( getRootCause() != null )
        {
            msg += ": " + getRootCause().getMessage();
        }

        return msg;
    }
}
