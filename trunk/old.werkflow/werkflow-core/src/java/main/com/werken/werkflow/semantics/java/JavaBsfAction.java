package com.werken.werkflow.semantics.java;

import com.werken.werkflow.bsf.BeanShellBSFEngine;
import com.werken.werkflow.bsf.BsfAction;

import org.apache.bsf.BSFException;

public class JavaBsfAction
    extends BsfAction
{
    static
    {
        Class junk = BeanShellBSFEngine.class;
    }

    public JavaBsfAction(String text)
        throws BSFException
    {
        super( "beanshell",
               text );
    }
}
