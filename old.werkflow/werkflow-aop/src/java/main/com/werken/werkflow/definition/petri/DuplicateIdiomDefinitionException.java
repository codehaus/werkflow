package com.werken.werkflow.definition.petri;

public class DuplicateIdiomDefinitionException
    extends IdiomDefinitionException
{
    private IdiomDefinition idiomDef;

    public DuplicateIdiomDefinitionException(IdiomDefinition idiomDef)
    {
        this.idiomDef = idiomDef;
    }

    public IdiomDefinition getIdiomDefinition()
    {
        return this.idiomDef;
    }

    public String getMessage()
    {
        return "duplicate idiom definition: " + getIdiomDefinition().getId();
    }
}
