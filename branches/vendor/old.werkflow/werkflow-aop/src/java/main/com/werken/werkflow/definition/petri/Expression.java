package com.werken.werkflow.definition.petri;

public interface Expression
{
    boolean evaluate(Parameters parameters)
        throws Exception;
}
