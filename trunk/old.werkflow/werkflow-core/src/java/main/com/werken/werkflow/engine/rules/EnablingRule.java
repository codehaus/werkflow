package com.werken.werkflow.engine.rules;

import com.werken.werkflow.engine.WorkflowProcessCase;
import com.werken.werkflow.definition.petri.ActivationRule;
import com.werken.werkflow.definition.petri.Transition;

public class EnablingRule
{
    private Transition transition;

    public EnablingRule(Transition transition)
    {
        this.transition = transition;
    }

    public Transition getTransition()
    {
        return this.transition;
    }

    public boolean evaluate(WorkflowProcessCase processCase)
    {
        ActivationRule rule = getTransition().getActivationRule();

        if ( rule != null )
        {
            try
            {
                if ( ! rule.isSatisfied( getTransition(),
                                         processCase ) )
                {
                    return false;
                }
            }
            catch (Exception e)
            {
                // FIXME: log
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }
}
