package com.werken.werkflow.semantics.python;

import com.werken.werkflow.bsf.BsfExpression;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.expression.Expression;

import org.apache.bsf.BSFException;

import org.python.core.PyInteger;
import org.python.core.PyString;

public class PythonExpression
    extends BsfExpression
{
    public PythonExpression(String text)
        throws BSFException
    {
        super( "jython",
               text );
    }

    public boolean asBoolean(Object value)
    {
        if ( value instanceof PyInteger )
        {
            return ( ((PyInteger)value).getValue() != 0 );
        }

        if ( value instanceof PyString)
        {
            String str = ((PyString)value).toString();

            return stringAsBoolean( str );
        }

        return false;
    }
}
