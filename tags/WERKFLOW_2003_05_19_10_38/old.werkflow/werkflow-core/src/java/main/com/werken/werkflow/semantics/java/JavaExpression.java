package com.werken.werkflow.semantics.java;

import com.werken.werkflow.bsf.BsfExpression;
import com.werken.werkflow.bsf.BeanShellBSFEngine;

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
        Class junk = BeanShellBSFEngine.class;
    }

    public JavaExpression(String text)
        throws BSFException
    {
        super( "beanshell",
               text );
    }
}
