package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.petri.IdiomDefinition;
import com.werken.werkflow.definition.petri.IdiomDefinitionLibrary;
import com.werken.werkflow.syntax.idiom.IdiomDefinitionLoader;

import junit.framework.TestCase;

import java.net.URL;

public class IdiomaticParserTest
    extends TestCase
{
    public void testLoad()
        throws Exception
    {
        URL idiomsUrl = getClass().getResource( "idioms1.xml" );

        IdiomDefinitionLoader loader = new IdiomDefinitionLoader();

        IdiomDefinition[] idiomDefs = loader.load( idiomsUrl );

        IdiomDefinitionLibrary idiomDefLib = new IdiomDefinitionLibrary();

        for ( int i = 0 ; i < idiomDefs.length ; ++i )
        {
            idiomDefLib.addIdiomDefinition( idiomDefs[i] );
        }

        URL idiomaticUrl = getClass().getResource( "idiomatic1.xml" );

        IdiomaticParser parser = new IdiomaticParser( idiomDefLib );

        parser.parse( idiomaticUrl );

    }
}
