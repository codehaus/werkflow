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
import java.util.Map;

public abstract class AbstractPersonality
    implements Personality
{
    private Syntax[] syntaxes;

    protected AbstractPersonality(Syntax[] syntaxes)
    {
        this.syntaxes = syntaxes;
    }

    protected AbstractPersonality(AbstractPersonality base)
    {
        this.syntaxes = base.getSyntaxes();
    }

    protected Syntax[] getSyntaxes()
    {
        Syntax[] copy;

        copy = new Syntax[syntaxes.length];
        System.arraycopy( syntaxes, 0, copy, 0, syntaxes.length );
        return copy;
    }

    public ProcessDefinition[] load(URL url)
        throws IOException, Exception
    {
        JellyContext context = newJellyContext( getSyntaxJellyContext() );

        return load( url,
                     context );
    }

    public ProcessDefinition[] load(URL url, Map beans )
        throws IOException, Exception
    {
        JellyContext context = newJellyContext( getSyntaxJellyContext(), beans );

        return load( url,
                     context );
    }

    protected abstract JellyContext getSyntaxJellyContext();

    protected JellyContext newJellyContext( JellyContext parent )
    {
        JellyContext context = JellyUtil.newJellyContext( parent );

        Syntax[] syntaxes = getSyntaxes();

        for ( int i = 0; i < syntaxes.length; ++i )
        {
            context.registerTagLibrary( syntaxes[i].getNamespaceUri(),
                                        syntaxes[i].getTagLibrary() );
        }

        return context;
    }

    protected JellyContext newJellyContext( JellyContext parent, Map beans )
    {
        JellyContext context = newJellyContext( parent );
        return context.newJellyContext( beans );
    }

    protected ProcessDefinition[] load( URL url,
                                        JellyContext context )
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

    protected static Syntax[] loadSyntaxes( URL url,
                                            JellyContext context )
        throws IOException, Exception
    {
        SyntaxLoader loader = new SyntaxLoader();

        return loader.load( url,
                            context );
    }
}
