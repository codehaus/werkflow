package com.werken.werkflow.definition.petri;

public class InvalidParameterTypeException
    extends IdiomException
{
    private IdiomParameter parameter;
    private Object value;

    public InvalidParameterTypeException(IdiomParameter parameter,
                                         Object value)
    {
        this.parameter = parameter;
        this.value     = value;
    }

    public IdiomParameter getParameter()
    {
        return this.parameter;
    }

    public Object getValue()
    {
        return this.value;
    }

    public String getMessage()
    {
        return "invalid parameter type for '" + getParameter().getId()
            + "': expected: " + getParameter().getType().getName()
            + " but received: " + getValue().getClass().getName()
            + " (" + getValue() + ")";
    }
}
