package com.werken.werkflow.definition.petri;

public class DefaultArc
    extends DefaultElement
    implements Arc
{
    private Place place;
    private Transition transition;
    private Expression expression;

    public DefaultArc(Place place,
                      Transition transition)
    {
        this.place = place;
        this.transition = transition;
    }

    public void setExpression(Expression expression)
    {
        this.expression = expression;
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    public Place getPlace()
    {
        return this.place;
    }

    public Transition getTransition()
    {
        return this.transition;
    }

}
