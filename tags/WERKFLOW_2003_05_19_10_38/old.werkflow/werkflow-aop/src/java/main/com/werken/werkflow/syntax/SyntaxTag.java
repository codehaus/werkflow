package com.werken.werkflow.syntax;

import com.werken.werkflow.jelly.MiscTagSupport;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class SyntaxTag
    extends MiscTagSupport
{
    private String uri;

    public SyntaxTag()
    {
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getUri()
    {
        return this.uri;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireStringAttribute( "uri",
                                getUri() );

        Syntax syntax = new Syntax( getUri() );

        getContext().setVariable( Syntax.class.getName(),
                                  syntax );

        invokeBody( output );

        getContext().removeVariable( Syntax.class.getName() );

        addToCollector( Syntax.class,
                        syntax );

        getContext().registerTagLibrary( getUri(),
                                         syntax.getTagLibrary() );
    }
}
