package com.werken.werkflow.definition.petri;

import com.werken.werkflow.engine.WorkflowProcessCase;

public interface ActivationRule
{
    boolean isSatisfied(Transition transition,
                        WorkflowProcessCase processCase)
        throws Exception;

    String [] satisfy(Transition transition,
                      WorkflowProcessCase processCase);
}
