package com.werken.werkflow.semantics.java;

import com.werken.werkflow.bsf.BsfExpression;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.expression.Expression;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

import org.python.core.PyInteger;
import org.python.core.PyString;

public class JavaExpression
    extends BsfExpression
{
    static
    {
        String [] extensions = { "bsh" };
        
        BSFManager.registerScriptingEngine(
            "beanshell",
            "com.werken.werkflow.bsf.BeanShellBSFEngine",
            extensions );
    }

    public JavaExpression(String text)
        throws BSFException
    {
        super( "beanshell",
               text );
    }

    public boolean asBoolean(Object value)
    {
        if ( value instanceof Integer )
        {
            return ( ((Integer)value).intValue() != 0 );
        }

        if ( value instanceof Boolean )
        {
            return ((Boolean)value).booleanValue();
        }

        if ( value instanceof String)
        {
            return stringAsBoolean( (String) value );
        }

        return false;
    }
}
