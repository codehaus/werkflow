package com.werken.werkflow.definition.petri;

public class DuplicateParameterException
    extends IdiomException
{
    private IdiomParameter parameter;

    public DuplicateParameterException(IdiomParameter parameter)
    {
        this.parameter = parameter;
    }

    public IdiomParameter getParameter()
    {
        return this.parameter;
    }
}
