package com.werken.werkflow.definition.petri;

public class NoSuchIdiomDefinitionException
    extends IdiomDefinitionException
{
    private String id;

    public NoSuchIdiomDefinitionException(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public String getMessage()
    {
        return "no such idiom definition: " + getId();
    }
}
