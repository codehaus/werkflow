package com.werken.werkflow.jelly;

import com.werken.werkflow.semantics.jelly.JellyTagLibrary;
import com.werken.werkflow.semantics.java.JavaTagLibrary;
import com.werken.werkflow.semantics.jexl.JexlTagLibrary;
import com.werken.werkflow.semantics.ognl.OgnlTagLibrary;
import com.werken.werkflow.semantics.python.PythonTagLibrary;

import org.apache.commons.jelly.JellyContext;

public class JellyUtil
{
    public static JellyContext newJellyContext()
    {
        JellyContext context = new JellyContext();

        registerTagLibraries( context );

        return context;
    }

    public static JellyContext newJellyContext(JellyContext parent)
    {
        JellyContext context = new JellyContext( parent );

        registerTagLibraries( context );

        return context;
    }

    public static void registerTagLibraries(JellyContext context)
    {
        context.registerTagLibrary( JellyTagLibrary.NS_URI,
                                    new JellyTagLibrary() );

        context.registerTagLibrary( JavaTagLibrary.NS_URI,
                                    new JavaTagLibrary() );

        context.registerTagLibrary( JexlTagLibrary.NS_URI,
                                    new JexlTagLibrary() );

        context.registerTagLibrary( OgnlTagLibrary.NS_URI,
                                    new OgnlTagLibrary() );

        context.registerTagLibrary( PythonTagLibrary.NS_URI,
                                    new PythonTagLibrary() );
    }
}
