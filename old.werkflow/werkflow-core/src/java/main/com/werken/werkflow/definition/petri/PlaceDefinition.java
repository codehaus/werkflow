package com.werken.werkflow.definition.petri;

public class PlaceDefinition
{
    public static final PlaceDefinition[] EMPTY_ARRAY = new PlaceDefinition[0];

    private String id;
    private String documentation;
    private String stashId;

    public PlaceDefinition(String id,
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

    public void setStashId(String stashId)
    {
        this.stashId = stashId;
    }

    public String getStashId()
    {
        return this.stashId;
    }

    public boolean equals(Object that)
    {
        if ( that instanceof PlaceDefinition )
        {
            return ((PlaceDefinition)that).getId().equals( getId() );
        }

        return false;
    }

    public int hashCode()
    {
        return getId().hashCode();
    }
}
