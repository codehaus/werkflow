package com.werken.werkflow.syntax;

import com.werken.werkflow.jelly.MiscTagSupport;
import com.werken.werkflow.syntax.fundamental.FundamentalTagLibrary;
import com.werken.werkflow.syntax.idiom.IdiomTagLibrary;
import com.werken.werkflow.syntax.petri.PetriTagLibrary;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.TagLibrary;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.impl.DynamicTagLibrary;
import org.apache.commons.jelly.parser.XMLParser;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class SyntaxLoader
{
    public Syntax[] load(URL url,
                         JellyContext evalContext)
        throws IOException, Exception
    {
        XMLParser parser = new XMLParser();

        JellyContext loadContext = new JellyContext();

        loadContext.registerTagLibrary( FundamentalTagLibrary.NS_URI,
                                        new FundamentalTagLibrary() );

        loadContext.registerTagLibrary( PetriTagLibrary.NAMESPACE_URI,
                                        new PetriTagLibrary() );

        loadContext.registerTagLibrary( IdiomTagLibrary.NAMESPACE_URI,
                                        new IdiomTagLibrary() );

        loadContext.registerTagLibrary( SyntaxTagLibrary.NAMESPACE_URI,
                                        new SyntaxTagLibrary() );

        parser.setContext( loadContext );

        Script script = parser.parse( url );

        XMLOutput output = XMLOutput.createXMLOutput( System.err );

        // JellyContext evalContext = new JellyContext();

        evalContext.registerTagLibrary( FundamentalTagLibrary.NS_URI,
                                        new FundamentalTagLibrary() );

        evalContext.registerTagLibrary( PetriTagLibrary.NAMESPACE_URI,
                                        new PetriTagLibrary() );

        evalContext.registerTagLibrary( IdiomTagLibrary.NAMESPACE_URI,
                                        new IdiomTagLibrary() );

        evalContext.registerTagLibrary( SyntaxTagLibrary.NAMESPACE_URI,
                                        new SyntaxTagLibrary() );

        evalContext.setCurrentURL( new URL( url,
                                            "." )  );

        
        List syntaxes = new ArrayList();

        MiscTagSupport.installCollector( Syntax.class,
                                         syntaxes,
                                         evalContext );

        
        script.run( evalContext,
                    output );

        return (Syntax[]) syntaxes.toArray( Syntax.EMPTY_ARRAY );
    }
}
