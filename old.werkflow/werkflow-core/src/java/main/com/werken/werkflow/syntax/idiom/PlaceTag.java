package com.werken.werkflow.syntax.idiom;

import com.werken.werkflow.definition.petri.IdiomDefinition;
import com.werken.werkflow.definition.petri.PlaceDefinition;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class PlaceTag
    extends IdiomTagSupport
{
    private String id;
    private String stash;
    private String documentation;

    public PlaceTag()
    {
        // intentionally left blank
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public void setStash(String stash)
    {
        this.stash = stash;
    }

    public String getStash()
    {
        return this.stash;
    }

    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    public String getDocumentation()
    {
        return this.documentation;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        IdiomDefinition idiomDef = getCurrentIdiomDefinition();

        requireStringAttribute( "id",
                                getId() );

        invokeBody( output );

        PlaceDefinition place = new PlaceDefinition( getId(),
                                                     getDocumentation() );

        if ( getStash() != null
             &&
             ! getStash().equals( "" ) )
        {
            place.setStashId( getStash() );
        }

        idiomDef.addPlace( place );
    }
}
