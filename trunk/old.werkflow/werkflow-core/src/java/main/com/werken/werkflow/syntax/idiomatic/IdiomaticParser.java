package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.petri.IdiomDefinitionLibrary;
import com.werken.werkflow.jelly.MiscTagSupport;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.parser.XMLParser;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class IdiomaticParser
{
    private IdiomDefinitionLibrary idiomDefLib;

    public IdiomaticParser(IdiomDefinitionLibrary idiomDefLib)
    {
        this.idiomDefLib = idiomDefLib;
    }

    public void parse(URL url)
        throws IOException, Exception
    {
        JellyContext context = new JellyContext();

        context.registerTagLibrary( IdiomaticTagLibrary.NAMESPACE_URI,
                                    new IdiomaticTagLibrary( this.idiomDefLib ) );

        XMLParser parser = new XMLParser();

        parser.setContext( context );

        Script script = parser.parse( url );

        /*
        List idiomDefs = new ArrayList();

        MiscTagSupport.installCollector( IdiomDefinition.class,
                                         idiomDefs,
                                         context );
        */

        // XMLOutput output = XMLOutput.createDummyXMLOutput();

        XMLOutput output = XMLOutput.createXMLOutput( System.err );

        script.run( context,
                    output );

        // return (IdiomDefinition[]) idiomDefs.toArray( IdiomDefinition.EMPTY_ARRAY );
    }
}
