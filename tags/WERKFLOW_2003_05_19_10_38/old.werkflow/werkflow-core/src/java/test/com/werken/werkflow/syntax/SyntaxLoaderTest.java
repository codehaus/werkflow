package com.werken.werkflow.syntax;

import org.apache.commons.jelly.JellyContext;

import junit.framework.TestCase;

import java.net.URL;

public class SyntaxLoaderTest
    extends TestCase
{
    public void testLoad()
        throws Exception
    {
        URL url = getClass().getResource( "syntax1.xml" );

        SyntaxLoader loader = new SyntaxLoader();

        JellyContext context = new JellyContext();

        Syntax[] syntaxes = loader.load( url,
                                         context );

        assertEquals( 1,
                      syntaxes.length );
    }
}
