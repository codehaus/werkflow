package com.werken.werkflow.syntax;

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

        Syntax[] syntaxes = loader.load( url );

        assertEquals( 1,
                      syntaxes.length );
    }
}
