package com.werken.werkflow.definition.petri;

import com.werken.werkflow.definition.MessageWaiter;
import com.werken.werkflow.task.Task;

public class DefaultTransition
    extends DefaultNode
    implements Transition
{
    private Expression expression;
    private ActivationRule activationRule;
    private MessageWaiter messageWaiter;
    private Task task;

    public DefaultTransition(String id)
    {
        super( id );
    }

    public Arc[] getArcsFromPlaces()
    {
        return getInboundArcs();
    }

    public Arc[] getArcsToPlaces()
    {
        return getOutboundArcs();
    }

    public void setExpression(Expression expression)
    {
        this.expression = expression;
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    public void setActivationRule(ActivationRule activationRule)
    {
        this.activationRule = activationRule;
    }

    public ActivationRule getActivationRule()
    {
        return this.activationRule;
    }

    public void setTask(Task task)
    {
        this.task = task;
    }

    public Task getTask()
    {
        return this.task;
    }

    public void setMessageWaiter(MessageWaiter messageWaiter)
    {
        this.messageWaiter = messageWaiter;
    }

    public MessageWaiter getMessageWaiter()
    {
        return this.messageWaiter;
    }
}
