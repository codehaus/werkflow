package com.werken.werkflow.definition.petri;

public interface NetSegment
{
    Place getPlace(String id);
    Place[] getPlaces();

    Transition getTransition(String id);
    Transition[] getTransitions();
}
