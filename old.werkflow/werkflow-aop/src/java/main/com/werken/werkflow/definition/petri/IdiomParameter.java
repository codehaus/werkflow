package com.werken.werkflow.definition.petri;

public class IdiomParameter
{
    public static final IdiomParameter[] EMPTY_ARRAY = new IdiomParameter[0];

    private String id;
    private Class type;
    private boolean required;

    public IdiomParameter(String id,
                          Class type,
                          boolean required)
    {
        this.id       = id;
        this.type     = type;
        this.required = required;
    }

    public String getId()
    {
        return this.id;
    }

    public Class getType()
    {
        return this.type;
    }

    public boolean isRequired()
    {
        return this.required;
    }
}
