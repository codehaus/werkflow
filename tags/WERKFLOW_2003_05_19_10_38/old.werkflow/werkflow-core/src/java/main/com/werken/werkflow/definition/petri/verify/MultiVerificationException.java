package com.werken.werkflow.definition.petri.verify;

import com.werken.werkflow.definition.petri.Net;

import java.util.List;
import java.util.ArrayList;

public class MultiVerificationException
    extends VerificationException
{
    private List exceptions;

    public MultiVerificationException(Net net)
    {
        super( net );

        this.exceptions = new ArrayList();
    }

    public void addVerificationException(VerificationException e)
    {
        if ( e instanceof MultiVerificationException)
        {
            this.exceptions.addAll( ((MultiVerificationException)e).exceptions );
        }
        else
        {
            this.exceptions.add( e );
        }
    }

    public VerificationException[] getVerificationExceptions()
    {
        return (VerificationException[]) this.exceptions.toArray( VerificationException.EMPTY_ARRAY );
    }
}
