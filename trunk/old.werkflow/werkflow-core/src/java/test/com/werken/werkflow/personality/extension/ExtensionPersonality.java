package com.werken.werkflow.personality.extension;

import org.apache.commons.jelly.JellyContext;

import com.werken.werkflow.jelly.JellyUtil;
import com.werken.werkflow.personality.AbstractPersonality;
import com.werken.werkflow.personality.Personality;
import com.werken.werkflow.personality.basic.BasicPersonality;

/**
 * Extend <code>BasicPersonality</code> by adding
 * <code>ExtensionTagLibrary</code>.
 *
 * @author Mark Wilkinson
 */
public class ExtensionPersonality extends BasicPersonality
{

    private static ExtensionPersonality instance = null;
    private static JellyContext context = null;

    public static Personality getInstance() throws Exception
    {
        synchronized ( ExtensionPersonality.class )
        {
            if ( instance == null )
            {
                AbstractPersonality basic;
                JellyContext basicContext;

                basic = (AbstractPersonality) BasicPersonality.getInstance();
                instance = new ExtensionPersonality( basic );
                basicContext = instance.getBaseSyntaxJellyContext();
                context = JellyUtil.newJellyContext( basicContext );
                context.registerTagLibrary( ExtensionTagLibrary.NAMESPACE_URI,
                                            new ExtensionTagLibrary() );
            }
        }
        return instance;
    }

    protected ExtensionPersonality(AbstractPersonality base)
    {
        super( base );
    }

    protected JellyContext getSyntaxJellyContext()
    {
        return context;
    }

    private JellyContext getBaseSyntaxJellyContext()
    {
        return super.getSyntaxJellyContext();
    }
}
