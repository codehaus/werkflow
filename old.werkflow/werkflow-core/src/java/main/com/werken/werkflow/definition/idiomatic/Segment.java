package com.werken.werkflow.definition.idiomatic;

import com.werken.werkflow.definition.Expression;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.definition.petri.PetriException;

public interface Segment
{
    void addSegment(Segment segment)
        throws UnsupportedIdiomException;

    DefaultTransition[] build(NetBuilder builder)
        throws PetriException;
}
