package com.werken.werkflow.syntax;

import com.werken.werkflow.jelly.JellyUtil;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.impl.DynamicTagLibrary;
import org.apache.commons.jelly.parser.XMLParser;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class ProcessLoader
{
    private Syntax[] syntaxes;

    public ProcessLoader(Syntax[] syntaxes)
    {
        this.syntaxes = syntaxes;
    }

    public void load(URL url)
        throws IOException, Exception
    {
        XMLParser parser = new XMLParser();

        JellyContext loadContext = JellyUtil.newJellyContext();

        for ( int i = 0 ; i < this.syntaxes.length ; ++i )
        {
            loadContext.registerTagLibrary( this.syntaxes[i].getNamespaceUri(),
                                            this.syntaxes[i].getTagLibrary() );
        }
        
        parser.setContext( loadContext );

        Script script = parser.parse( url );

        XMLOutput output = XMLOutput.createXMLOutput( System.err );

        JellyContext evalContext = JellyUtil.newJellyContext();

        script.run( evalContext,
                    output );
    }
}
