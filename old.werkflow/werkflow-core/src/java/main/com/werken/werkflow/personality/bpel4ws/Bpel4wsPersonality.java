package com.werken.werkflow.personality.bpel4ws;

import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.syntax.Syntax;
import com.werken.werkflow.jelly.JellyUtil;

import com.werken.werkflow.personality.AbstractPersonality;
import com.werken.werkflow.personality.Personality;

import org.apache.commons.jelly.JellyContext;

import java.io.IOException;
import java.net.URL;

public class Bpel4wsPersonality
    extends AbstractPersonality
{
    public static final String SYNTAX_DEF = "bpel4ws-syntax.xml";

    private static Bpel4wsPersonality INSTANCE = null;
    private static JellyContext CONTEXT = null;

    public static Personality getInstance()
        throws Exception
    {
        synchronized ( Bpel4wsPersonality.class )
        {
            if ( INSTANCE == null )
            {
                URL syntaxUrl = Bpel4wsPersonality.class.getResource( SYNTAX_DEF );

                CONTEXT = JellyUtil.newJellyContext();

                Syntax[] syntaxes = loadSyntaxes( syntaxUrl,
                                                  CONTEXT );

                INSTANCE = new Bpel4wsPersonality( syntaxes );
            }
        }

        return INSTANCE;
    }

    protected JellyContext getSyntaxJellyContext()
    {
        return CONTEXT;
    }

    protected Bpel4wsPersonality(Syntax[] syntaxes)
    {
        super( syntaxes );
    }

    protected Bpel4wsPersonality(AbstractPersonality base)
    {
        super( base );
    }
}
