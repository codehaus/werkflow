package com.werken.werkflow.syntax.idiom;

import com.werken.werkflow.definition.petri.IdiomDefinition;

import junit.framework.TestCase;

import java.net.URL;

public class IdiomDefinitionLoaderTest
    extends TestCase
{
    public void testLoad()
        throws Exception
    {
        URL idiomsUrl = getClass().getResource( "idioms1.xml" );

        IdiomDefinitionLoader loader = new IdiomDefinitionLoader();

        IdiomDefinition[] idiomDefs = loader.load( idiomsUrl );

        assertEquals( 2,
                      idiomDefs.length );

        assertEquals( "empty",
                      idiomDefs[0].getId() );

        System.err.println( idiomDefs[0] );

        assertEquals( "parallel",
                      idiomDefs[1].getId() );

        System.err.println( idiomDefs[1] );
    }
}
