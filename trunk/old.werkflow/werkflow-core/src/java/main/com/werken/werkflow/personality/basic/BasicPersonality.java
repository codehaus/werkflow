package com.werken.werkflow.personality.basic;

import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.syntax.Syntax;
import com.werken.werkflow.jelly.JellyUtil;

import com.werken.werkflow.personality.AbstractPersonality;

import org.apache.commons.jelly.JellyContext;

import java.io.IOException;
import java.net.URL;

public class BasicPersonality
    extends AbstractPersonality
{
    public static final String SYNTAX_DEF = "basic-syntax.xml";

    private static BasicPersonality INSTANCE = null;
    private static JellyContext CONTEXT = null;

    public static BasicPersonality getInstance()
        throws Exception
    {
        synchronized ( BasicPersonality.class )
        {
            if ( INSTANCE == null )
            {
                URL syntaxUrl = BasicPersonality.class.getResource( SYNTAX_DEF );

                CONTEXT = JellyUtil.newJellyContext();
                
                Syntax[] syntaxes = loadSyntaxes( BasicPersonality.class.getResource( SYNTAX_DEF ),
                                                  CONTEXT );

                INSTANCE = new BasicPersonality( syntaxes );
            }
        }

        return INSTANCE;
    }

    protected BasicPersonality(Syntax[] syntaxes)
    {
        super( syntaxes  );
    }

    public ProcessDefinition[] load(URL url)
        throws IOException, Exception
    {
        JellyContext context = newJellyContext( CONTEXT );

        return load( url,
                     context );
    }

}
