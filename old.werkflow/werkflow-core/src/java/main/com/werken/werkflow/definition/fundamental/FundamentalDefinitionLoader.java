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

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class FundamentalDefinitionLoader
    implements DefinitionLoader
{
    public static final String FUNDAMENTAL_TAGLIB_NS_URI = "werkflow:fundamental";
    public static final String JAVA_TAGLIB_NS_URI = "werkflow:java";
    public static final String JELLY_TAGLIB_NS_URI = "werkflow:jelly";
    public static final String MESSAGE_TYPES_FILENAME = "message-types.xml";

    static final String MESSAGE_TYPE_LIBRARY_KEY = "werkflow.msg.type.lib";
    static final String FUNDAMENTAL_DEFINITION_LIST = "werkflow.fundamental.definition-list";

    public FundamentalDefinitionLoader()
    {
    }

    public ProcessDefinition[] loadFlowArchve(File flowArchive)
        throws Exception
    {
        if ( flowArchive.isDirectory() )
        {
            return loadDirectoryFlowArchive( flowArchive );
        }

        return loadFlowArchive( flowArchive.toURL() );
    }

    public ProcessDefinition[] loadDirectoryFlowArchive(File archiveDir)
        throws Exception
    {
        File messageTypesXml = new File( archiveDir,
                                         MESSAGE_TYPES_FILENAME );

        MessageTypeLibrary globalMsgTypeLib = null;

        try
        {
            if ( messageTypesXml.exists() )
            {
                globalMsgTypeLib = loadMessageTypeLibrary( messageTypesXml.toURL() );
            }
        }
        catch (FileNotFoundException e)
        {
            globalMsgTypeLib = new MessageTypeLibrary();
        }

        File[] children = archiveDir.listFiles();

        List defs = new ArrayList();

        for ( int i = 0 ; i < children.length ; ++i )
        {
            if ( ! children[i].getName().equals( MESSAGE_TYPES_FILENAME ) )
            {
                ProcessDefinition[] scriptDefs = null;

                scriptDefs = load( children[i].toURL(),
                                   new MessageTypeLibrary( globalMsgTypeLib ) );

                for ( int j = 0 ; j < scriptDefs.length ; ++j )
                {
                    defs.add( scriptDefs[j] );
                }
            }
        }

        return (ProcessDefinition[]) defs.toArray( ProcessDefinition.EMPTY_ARRAY );
    }

    public ProcessDefinition[] loadFlowArchive(URL flowArchive)
        throws Exception
    {
        return null;
    }

    public ProcessDefinition[] load(URL url)
        throws Exception
    {
        MessageTypeLibrary msgTypeLib = null;

        try
        {
            msgTypeLib = loadMessageTypeLibrary( new URL( url,
                                                          MESSAGE_TYPES_FILENAME ) );
        }
        catch (FileNotFoundException e)
        {
            msgTypeLib = new MessageTypeLibrary();
        }
        
        return load( url,
                     msgTypeLib );
    }

    public ProcessDefinition[] load(URL url,
                                    MessageTypeLibrary msgTypeLib)
        throws Exception
    {
        XMLParser    parser  = new XMLParser();
        JellyContext context = new JellyContext();

        context.registerTagLibrary( FUNDAMENTAL_TAGLIB_NS_URI,
                                    new FundamentalTagLibrary() );

        context.registerTagLibrary( JAVA_TAGLIB_NS_URI,
                                    new JavaTagLibrary() );

        context.registerTagLibrary( JELLY_TAGLIB_NS_URI,
                                    new JellyTagLibrary() );

        context.setVariable( MESSAGE_TYPE_LIBRARY_KEY,
                             msgTypeLib );

        parser.setContext( context );

        Script script = parser.parse( url.toExternalForm() );

        List defs = new ArrayList();

        context.setVariable( FUNDAMENTAL_DEFINITION_LIST,
                             defs );

        script.run( context,
                    XMLOutput.createDummyXMLOutput() );

        return (ProcessDefinition[]) defs.toArray( ProcessDefinition.EMPTY_ARRAY );
    }

    public MessageTypeLibrary loadMessageTypeLibrary(URL url)
        throws Exception
    {
        XMLParser    parser  = new XMLParser();
        JellyContext context = new JellyContext();
        
        context.registerTagLibrary( FUNDAMENTAL_TAGLIB_NS_URI,
                                    new FundamentalTagLibrary() );
        
        context.registerTagLibrary( JAVA_TAGLIB_NS_URI,
                                    new JavaTagLibrary() );
        
        context.registerTagLibrary( JELLY_TAGLIB_NS_URI,
                                    new JellyTagLibrary() );
        
        MessageTypeLibrary msgTypeLib = new MessageTypeLibrary();

        context.setVariable( MESSAGE_TYPE_LIBRARY_KEY,
                             msgTypeLib );

        parser.setContext( context );

        Script script = parser.parse( url.toExternalForm() );

        script.run( context,
                    XMLOutput.createDummyXMLOutput() );

        return msgTypeLib;
    }
}
