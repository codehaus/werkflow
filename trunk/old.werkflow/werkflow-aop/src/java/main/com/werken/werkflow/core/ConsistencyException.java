package com.werken.werkflow.core;

class ConsistencyException
    extends RuntimeException
{
    ConsistencyException(String message)
    {
        super( message );
    }
}
