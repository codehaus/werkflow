package com.werken.werkflow.personality;

import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.jelly.MiscTagSupport;
import com.werken.werkflow.jelly.JellyUtil;
import com.werken.werkflow.syntax.Syntax;
import com.werken.werkflow.syntax.SyntaxLoader;
import com.werken.werkflow.syntax.fundamental.FundamentalTagLibrary;

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
    private Syntax[] syntaxes;

    public AbstractPersonality(Syntax[] syntaxes)
    {
        this.syntaxes = syntaxes;
    }

    public Syntax[] getSyntaxes()
    {
        return this.syntaxes;
    }

    protected JellyContext newJellyContext(JellyContext parent)
    {
        JellyContext context = JellyUtil.newJellyContext( parent );

        Syntax[] syntaxes = getSyntaxes();

        for ( int i = 0 ; i < syntaxes.length ; ++i )
        {
            context.registerTagLibrary( syntaxes[i].getNamespaceUri(),
                                        syntaxes[i].getTagLibrary() );
        }

	/*
        context.registerTagLibrary( FundamentalTagLibrary.NS_URI,
                                    new FundamentalTagLibrary() );
	*/

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
                    XMLOutput.createXMLOutput( System.err ) );

        return (ProcessDefinition[]) processDefs.toArray( ProcessDefinition.EMPTY_ARRAY );
    }

    protected static Syntax[] loadSyntaxes(URL url,
                                           JellyContext context)
        throws IOException, Exception
    {
        SyntaxLoader loader = new SyntaxLoader();

        return loader.load( url,
                            context );
    }
}
