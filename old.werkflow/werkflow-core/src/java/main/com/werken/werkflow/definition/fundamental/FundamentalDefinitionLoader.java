package com.werken.werkflow.definition.fundamental;

/*
 $Id$

 Copyright 2003 (C) The Werken Company. All Rights Reserved.
 
 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.
 
 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.
 
 3. The name "werkflow" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.
 
 4. Products derived from this Software may not be called "werkflow"
    nor may "werkflow" appear in their names without prior written
    permission of The Werken Company. "werkflow" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://werkflow.werken.com/).
 
 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.
 
 */

import com.werken.werkflow.action.ActionLibrary;
import com.werken.werkflow.definition.DefinitionLoader;
import com.werken.werkflow.definition.ProcessDefinition;
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

/** <code>ProcessDefinition</code> loader for the fundamenatal syntax.
 *
 *  @see FundamentalTagLibrary
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class FundamentalDefinitionLoader
    implements DefinitionLoader
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    /** Message-types library filename. */
    public static final String MESSAGE_TYPES_FILENAME = "message-types.xml";

    /** Actions library filename. */
    public static final String ACTIONS_FILENAME = "actions.xml";

    /** Message-type library context key. */
    static final String MESSAGE_TYPE_LIBRARY_KEY = "werkflow.msg.type.lib";

    /** Action library context key. */
    static final String ACTION_LIBRARY_KEY = "werkflow.action.lib";

    /** Definition list context key. */
    static final String FUNDAMENTAL_DEFINITION_LIST = "werkflow.fundamental.definition-list";

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public FundamentalDefinitionLoader()
    {
        // intentionally left blank.
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Load a flow archive.
     *
     *  @param flowArchive The flow archive.
     *
     *  @return The process definitions in the archive.
     *
     *  @throws Exception If an error occurs while attempting to load
     *          the archive.
     */
    public ProcessDefinition[] loadFlowArchve(File flowArchive)
        throws Exception
    {
        if ( flowArchive.isDirectory() )
        {
            return loadDirectoryFlowArchive( flowArchive );
        }

        return loadFlowArchive( flowArchive.toURL() );
    }

    /** Load a directory flow archive.
     *
     *  @param archiveDir The archive directory.
     *
     *  @return The process definitions in the archive.
     *
     *  @throws Exception If an error occurs while attempting to load
     *          the archive.
     */
    public ProcessDefinition[] loadDirectoryFlowArchive(File archiveDir)
        throws Exception
    {
        File actionsXml = new File( archiveDir,
                                    ACTIONS_FILENAME );

        ActionLibrary globalActionLib = loadOptionalActionLibrary( actionsXml.toURL() );

        File messageTypesXml = new File( archiveDir,
                                         MESSAGE_TYPES_FILENAME );

        MessageTypeLibrary globalMsgTypeLib = loadOptionalMessageTypeLibrary( messageTypesXml.toURL() );

        File[] children = archiveDir.listFiles();

        List defs = new ArrayList();

        for ( int i = 0 ; i < children.length ; ++i )
        {
            if (  ( ! children[i].getName().equals( MESSAGE_TYPES_FILENAME ) )
                  &&
                  ( ! children[i].getName().equals( ACTIONS_FILENAME ) ) )
            {
                ProcessDefinition[] scriptDefs = null;

                scriptDefs = load( children[i].toURL(),
                                   new ActionLibrary( globalActionLib ),
                                   new MessageTypeLibrary( globalMsgTypeLib ) );

                for ( int j = 0 ; j < scriptDefs.length ; ++j )
                {
                    defs.add( scriptDefs[j] );
                }
            }
        }

        return (ProcessDefinition[]) defs.toArray( ProcessDefinition.EMPTY_ARRAY );
    }

    /** Load a flow archive.
     *
     *  <p>
     *  <b>NOT IMPLEMENTED</b>
     *  </p>
     *
     *  @param flowArchive The flow archive URL.
     *
     *  @return The process definitions in the archive.
     *
     *  @throws Exception If an error occurs while attempting to load
     *          the archive.
     */
    public ProcessDefinition[] loadFlowArchive(URL flowArchive)
        throws Exception
    {
        return null;
    }

    /** @see DefinitionLoader
     */
    public ProcessDefinition[] load(URL url)
        throws Exception
    {
        ActionLibrary actionLib = loadOptionalActionLibrary( new URL( url,
                                                                      ACTIONS_FILENAME ) );

        MessageTypeLibrary msgTypeLib = loadOptionalMessageTypeLibrary( new URL( url,
                                                                                 MESSAGE_TYPES_FILENAME ) );

        return load( url,
                     actionLib,
                     msgTypeLib );
    }

    /** Load a definition file.
     *
     *  <p>
     *  Load a process-definition file within the context of
     *  a pre-configured <code>ActionLibrary</code> and
     *  <code>MessageTypeLibrary</code>.
     *  </p>
     *
     *  @param url The definition URL.
     *  @param actionLib The action library.
     *  @param msgTypeLib The message-type library.
     *
     *  @return The loaded process definitions.
     *
     *  @throws Exception If an error occurs while attempting 
     *          to load the library.
     */
    public ProcessDefinition[] load(URL url,
                                    ActionLibrary actionLib,
                                    MessageTypeLibrary msgTypeLib)
        throws Exception
    {
        XMLParser    parser  = new XMLParser();
        JellyContext context = new JellyContext();

        context.registerTagLibrary( FundamentalTagLibrary.NS_URI,
                                    new FundamentalTagLibrary() );

        context.registerTagLibrary( JavaTagLibrary.NS_URI,
                                    new JavaTagLibrary() );

        context.registerTagLibrary( JellyTagLibrary.NS_URI,
                                    new JellyTagLibrary() );

        context.setVariable( ACTION_LIBRARY_KEY,
                             actionLib );

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

    /** Load an <code>MessageTypeLibrary</code>.
     *
     *  @param url The URL to load.
     *
     *  @return The message-type library.
     *
     *  @throws Exception If an error occurs while attempting 
     *          to load the library.
     */
    public MessageTypeLibrary loadMessageTypeLibrary(URL url)
        throws Exception
    {
        XMLParser    parser  = new XMLParser();
        JellyContext context = new JellyContext();
        
        context.registerTagLibrary( FundamentalTagLibrary.NS_URI,
                                    new FundamentalTagLibrary() );
        
        context.registerTagLibrary( JavaTagLibrary.NS_URI,
                                    new JavaTagLibrary() );
        
        context.registerTagLibrary( JellyTagLibrary.NS_URI,
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

    /** Load optional <code>message-types.xml</code> <code>MessageTypeLibrary</code>.
     *
     *  @param url The possibly invalid URL to an optional message-type library.
     *
     *  @return An action library, either empty due to non-existence,
     *          or the loaded library.
     *
     *  @throws Exception If a core error occurs while attempting to
     *          load the library.
     */
    public MessageTypeLibrary loadOptionalMessageTypeLibrary(URL url)
        throws Exception
    {
        try
        {
            return loadMessageTypeLibrary( url );
        }
        catch (FileNotFoundException e)
        {
            return new MessageTypeLibrary();
        }
    }

    /** Load an <code>ActionLibrary</code>.
     *
     *  @param url The URL to load.
     *
     *  @return The action library.
     *
     *  @throws Exception If an error occurs while attempting 
     *          to load the library.
     */
    public ActionLibrary loadActionLibrary(URL url)
        throws Exception
    {
        XMLParser    parser  = new XMLParser();
        JellyContext context = new JellyContext();
        
        context.registerTagLibrary( FundamentalTagLibrary.NS_URI,
                                    new FundamentalTagLibrary() );
        
        context.registerTagLibrary( JavaTagLibrary.NS_URI,
                                    new JavaTagLibrary() );
        
        context.registerTagLibrary( JellyTagLibrary.NS_URI,
                                    new JellyTagLibrary() );
        
        ActionLibrary actionLib = new ActionLibrary();

        context.setVariable( ACTION_LIBRARY_KEY,
                             actionLib );

        parser.setContext( context );

        Script script = parser.parse( url.toExternalForm() );

        script.run( context,
                    XMLOutput.createDummyXMLOutput() );

        return actionLib;
    }

    /** Load optional <code>actions.xml</code> <code>ActionLibrary</code>.
     *
     *  @param url The possibly invalid URL to an optional action library.
     *
     *  @return An action library, either empty due to non-existence,
     *          or the loaded library.
     *
     *  @throws Exception If a core error occurs while attempting to
     *          load the library.
     */
    public ActionLibrary loadOptionalActionLibrary(URL url)
        throws Exception
    {
        try
        {
            return loadActionLibrary( url );
        }
        catch (FileNotFoundException e)
        {
            return new ActionLibrary();
        }
    }
}
