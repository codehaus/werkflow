package com.werken.werkflow.definition.petri;

public interface Place
    extends Node
{
    static final Place[] EMPTY_ARRAY = new Place[0];

    Arc[] getArcsToTransitions();
    Arc[] getArcsFromTransitions();
}
