package com.werken.werkflow.definition.petri;

public interface Net
{
    Place[] getPlaces();

    Transition[] getTransitions();

    Transition getTransitionById(String id)
        throws NoSuchTransitionException;

    Arc[] getArcs();
}
