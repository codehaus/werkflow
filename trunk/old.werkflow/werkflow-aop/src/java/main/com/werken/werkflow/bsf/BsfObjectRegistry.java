package com.werken.werkflow.bsf;

import org.apache.bsf.util.ObjectRegistry;

import java.util.Map;
import java.util.HashMap;

public class BsfObjectRegistry
    extends ObjectRegistry
{
    private Map reg;

    public BsfObjectRegistry()
    {
        this.reg = new HashMap();
    }

    public void register(String name,
                         Object obj)
    {
        this.reg.put( name,
                      obj );
    }

    public void unregister(String name)
    {
        this.reg.remove( name );
    }

    public Object lookup(String name)
    {
        if ( ! this.reg.containsKey( name ) )
        {
            throw new IllegalArgumentException( "object '" + name + "' not in registry" );
        }

        return this.reg.get( name );
    }
}
