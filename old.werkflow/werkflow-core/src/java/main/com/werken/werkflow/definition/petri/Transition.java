package com.werken.werkflow.definition.petri;

import com.werken.werkflow.task.Task;

public interface Transition
    extends Node
{
    static final Transition[] EMPTY_ARRAY = new Transition[0];

    Arc[] getArcsFromPlaces();
    Arc[] getArcsToPlaces();

    Expression getExpression();

    ActivationRule getActivationRule();

    Task getTask();
}
