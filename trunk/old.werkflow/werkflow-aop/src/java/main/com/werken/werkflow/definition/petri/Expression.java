package com.werken.werkflow.definition.petri;

import com.werken.werkflow.Attributes;

public interface Expression
{
    boolean evaluate(Attributes attributes)
        throws Exception;
}
