package com.werken.werkflow.definition.petri;

public class MissingParameterException
    extends IdiomException
{
    private IdiomParameter parameter;

    public MissingParameterException(IdiomParameter parameter)
    {
        this.parameter = parameter;
    }

    public IdiomParameter getParameter()
    {
        return this.parameter;
    }
}
