package com.werken.werkflow.syntax.idiom;

import com.werken.werkflow.definition.petri.IdiomDefinition;
import com.werken.werkflow.jelly.MiscTagSupport;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.parser.XMLParser;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class IdiomDefinitionLoader
{
    public IdiomDefinitionLoader()
    {

    }

    public IdiomDefinition[] load(URL url)
        throws IOException, Exception
    {
        JellyContext context = new JellyContext();

        context.registerTagLibrary( IdiomTagLibrary.NAMESPACE_URI,
                                    new IdiomTagLibrary() );

        XMLParser parser = new XMLParser();

        parser.setContext( context );

        Script script = parser.parse( url );

        List idiomDefs = new ArrayList();

        MiscTagSupport.installCollector( IdiomDefinition.class,
                                         idiomDefs,
                                         context );

        // XMLOutput output = XMLOutput.createDummyXMLOutput();
        XMLOutput output = XMLOutput.createXMLOutput( System.err );

        script.run( context,
                    output );

        return (IdiomDefinition[]) idiomDefs.toArray( IdiomDefinition.EMPTY_ARRAY );
    }
}
