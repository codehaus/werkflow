package com.werken.werkflow.syntax.idiom;

import com.werken.werkflow.definition.petri.IdiomDefinition;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class IdiomTag
    extends IdiomTagSupport
{
    private String id;
    private String contains;

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

    public void setContains(String contains)
    {
        this.contains = contains;
    }

    public String getContains()
    {
        return this.contains;
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

        short containsType;

        if ( this.contains == null )
        {
            containsType = IdiomDefinition.CONTAINS_MULTI_COMPONENTS;
        }
        else if ( "none".equals( this.contains ) )
        {
            containsType = IdiomDefinition.CONTAINS_NONE;
        }
        else if ( "action".equals( this.contains ) )
        {
            containsType = IdiomDefinition.CONTAINS_ONE_ACTION;
        }
        else if ( "component".equals( this.contains ) )
        {
            containsType = IdiomDefinition.CONTAINS_ONE_COMPONENT;
        }
        else if ( this.contains == null
                  ||
                  "components".equals( this.contains )
                  ||
                  "".equals( this.contains ) )
        {
            containsType = IdiomDefinition.CONTAINS_MULTI_COMPONENTS;
        }
        else
        {
            throw new JellyTagException( "attribute 'contains' may only accept: action, component, components" );
        }

        IdiomDefinition idiomDef = new IdiomDefinition( tag.getUri(),
                                                        getId(),
                                                        containsType );

        setCurrentIdiomDefinition( idiomDef );

        invokeBody( output );

        setCurrentIdiomDefinition( null );

        addToCollector( IdiomDefinition.class,
                        idiomDef );
    }
}
