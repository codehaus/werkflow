package com.werken.werkflow.syntax.idiom;

import com.werken.werkflow.definition.petri.IdiomDefinition;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

import java.util.ArrayList;
import java.util.Collection;

public class IdiomsTag
    extends IdiomTagSupport
{
    private String uri;

    public IdiomsTag()
    {
        // intentionally left blank
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

        Collection idiomDefs = getCollector( IdiomDefinition.class );

        if ( idiomDefs == null )
        {
            idiomDefs = new ArrayList();

            installCollector( IdiomDefinition.class,
                              idiomDefs );

        }

        invokeBody( output );

        IdiomDefinition[] defArray = (IdiomDefinition[]) idiomDefs.toArray( IdiomDefinition.EMPTY_ARRAY );

        IdiomaticTagLibrary tagLib = new IdiomaticTagLibrary( defArray );

        getContext().registerTagLibrary( getUri(),
                                         tagLib );
    }
}
