package com.werken.werkflow.semantics.python;

import com.werken.werkflow.bsf.BeanShellBSFEngine;
import com.werken.werkflow.bsf.BsfAction;

import org.apache.bsf.BSFException;

public class PythonBsfAction
    extends BsfAction
{
    public PythonBsfAction(String text)
        throws BSFException
    {
        super( "jython",
               text );
    }
}
