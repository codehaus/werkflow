package com.werken.werkflow.definition.petri;

public class TransitionDefinition
{
    public static final TransitionDefinition[] EMPTY_ARRAY = new TransitionDefinition[0];

    private String id;
    private String documentation;

    public TransitionDefinition(String id,
                                String documentation)
    {
        this.id            = id;
        this.documentation = documentation;
    }

    public String getId()
    {
        return this.id;
    }

    public String getDocumentation()
    {
        return this.documentation;
    }

    public boolean equals(Object that)
    {
        if ( that instanceof TransitionDefinition )
        {
            return ((TransitionDefinition)that).getId().equals( getId() );
        }

        return false;
    }

    public int hashCode()
    {
        return getId().hashCode();
    }

    public String toString()
    {
        return "[TransitionDefinition: id=" + this.id + "]";
    }
}
