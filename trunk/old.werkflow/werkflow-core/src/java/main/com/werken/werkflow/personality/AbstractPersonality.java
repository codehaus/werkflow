package com.werken.werkflow.personality;

import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.jelly.MiscTagSupport;
import com.werken.werkflow.syntax.Syntax;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.parser.XMLParser;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public abstract class AbstractPersonality
    implements Personality
{
    private Syntax syntax;

    public AbstractPersonality(Syntax syntax)
    {
        this.syntax = syntax;
    }

    public Syntax getSyntax()
    {
        return this.syntax;
    }

    protected JellyContext newJellyContext()
    {
        JellyContext context = new JellyContext();

        context.registerTagLibrary( getSyntax().getNamespaceUri(),
                                    getSyntax().getTagLibrary() );

        return context;
    }

    protected ProcessDefinition[] load(URL url,
                                       JellyContext context)
        throws IOException, Exception
    {
        List processDefs = new ArrayList();

        XMLParser parser = new XMLParser();

        parser.setContext( context );

        Script script = parser.parse( url );

        MiscTagSupport.installCollector( ProcessDefinition.class,
                                         processDefs,
                                         context );

        script.run( context,
                    XMLOutput.createDummyXMLOutput() );

        return (ProcessDefinition[]) processDefs.toArray( ProcessDefinition.EMPTY_ARRAY );
    }
}
