package com.werken.werkflow.semantics.jelly;

import org.apache.commons.jelly.JellyContext;

import java.util.Map;

public class ActionJellyContext
    extends JellyContext
{
    private Map caseAttrs;
    private Map otherAttrs;

    public ActionJellyContext(Map caseAttrs,
                              Map otherAttrs)
    {
        this.caseAttrs  = caseAttrs;
        this.otherAttrs = otherAttrs;
    }

    public void setVariable(String key,
                            Object value)
    {
        this.caseAttrs.put( key,
                            value );
    }

    public Object getVariable(String key)
    {
        if ( this.caseAttrs.containsKey( key ) )
        {
            return this.caseAttrs.get( key );
        }

        return this.otherAttrs.get( key );
    }

    public Object findVariable(String key)
    {
        return getVariable( key );
    }

    public void removeVariable(String key)
    {
        this.caseAttrs.remove( key );
        this.otherAttrs.remove( key );
    }
}
