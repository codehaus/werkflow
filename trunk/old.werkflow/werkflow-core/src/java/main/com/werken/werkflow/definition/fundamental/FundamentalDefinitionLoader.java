package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.definition.DefinitionLoader;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.MessageTypeLibrary;
import com.werken.werkflow.semantics.java.JavaTagLibrary;
import com.werken.werkflow.semantics.jelly.JellyTagLibrary;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.parser.XMLParser;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class FundamentalDefinitionLoader
    implements DefinitionLoader
{
    public static final String FUNDAMENTAL_TAGLIB_NS_URI = "werkflow:fundamental";
    public static final String JAVA_TAGLIB_NS_URI = "werkflow:java";
    public static final String JELLY_TAGLIB_NS_URI = "werkflow:jelly";

    static final String MESSAGE_TYPE_LIBRARY_KEY = "werkflow.msg.type.lib";
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

        context.registerTagLibrary( JAVA_TAGLIB_NS_URI,
                                    new JavaTagLibrary() );

        context.registerTagLibrary( JELLY_TAGLIB_NS_URI,
                                    new JellyTagLibrary() );

        parser.setContext( context );

        MessageTypeLibrary msgTypeLib = new MessageTypeLibrary();

        try
        {
            Script msgTypeLibScript = parser.parse( new URL( url,
                                                             "message-types.xml" ).toExternalForm() );
            context.setVariable( MESSAGE_TYPE_LIBRARY_KEY,
                                 msgTypeLib );

            msgTypeLibScript.run( context,
                                  XMLOutput.createDummyXMLOutput() );
        }
        catch (IOException e)
        {
            // swallow
        }
            
        Script script = parser.parse( url.toExternalForm() );

        List defs = new ArrayList();

        context.setVariable( FUNDAMENTAL_DEFINITION_LIST,
                             defs );

        script.run( context,
                    XMLOutput.createDummyXMLOutput() );

        return (ProcessDefinition[]) defs.toArray( ProcessDefinition.EMPTY_ARRAY );
    }
}
