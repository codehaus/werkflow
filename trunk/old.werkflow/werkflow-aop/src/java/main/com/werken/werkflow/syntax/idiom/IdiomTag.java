package com.werken.werkflow.syntax.idiom;

import com.werken.werkflow.definition.petri.IdiomDefinition;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class IdiomTag
    extends IdiomTagSupport
{
    private String id;

    public IdiomTag()
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

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireStringAttribute( "id",
                                getId() );

        IdiomsTag tag = (IdiomsTag) findAncestorWithClass( IdiomsTag.class );

        if ( tag == null )
        {
            throw new JellyTagException( "invalid context for <idiom>" );
        }

        IdiomDefinition idiomDef = new IdiomDefinition( tag.getUri(),
                                                        getId() );

        setCurrentIdiomDefinition( idiomDef );

        invokeBody( output );

        setCurrentIdiomDefinition( null );

        addToCollector( IdiomDefinition.class,
                        idiomDef );
    }
}
