package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.definition.DefinitionLoader;
import com.werken.werkflow.definition.ProcessDefinition;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.parser.XMLParser;

import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class FundamentalDefinitionLoader
    implements DefinitionLoader
{
    public static final String FUNDAMENTAL_TAGLIB_NS_URI = "http://werkflow.werken.com/fundamental/1.0/";

    static final String FUNDAMENTAL_DEFINITION_LIST = "werkflow.fundamental.definition-list";

    public FundamentalDefinitionLoader()
    {
    }

    public ProcessDefinition[] load(URL url)
        throws Exception
    {
        XMLParser parser = new XMLParser();

        JellyContext context = new JellyContext();

        context.registerTagLibrary( FUNDAMENTAL_TAGLIB_NS_URI,
                                    new FundamentalTagLibrary() );

        parser.setContext( context );

        Script script = parser.parse( url.toExternalForm() );

        List defs = new ArrayList();

        context.setVariable( FUNDAMENTAL_DEFINITION_LIST,
                             defs );

        script.run( context,
                    XMLOutput.createDummyXMLOutput() );

        return (ProcessDefinition[]) defs.toArray( ProcessDefinition.EMPTY_ARRAY );
    }
}
