package com.werken.werkflow.bsf;

import com.werken.werkflow.expr.ExpressionContext;

import org.apache.bsf.util.ObjectRegistry;

public class ContextObjectRegistry
    extends ObjectRegistry
{
    public ContextObjectRegistry(ExpressionContext context)
    {
        String[] names = context.getNames();

        for ( int i = 0 ; i < names.length ; ++i )
        {
            register( names[i],
                      context.getValue( names[i] ) );
        }
    }
}
