package com.werken.werkflow.definition.petri;

public interface Arc
{
    static final Arc[] EMPTY_ARRAY = new Arc[0];

    String getDocumentation();

    Expression getExpression();

    Place getPlace();

    Transition getTransition();
}
