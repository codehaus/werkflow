package com.werken.werkflow.definition.fundamental;

import org.apache.commons.jelly.JellyContext;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class OverlayJellyContext
    extends JellyContext
{
    private Map caseAttrs;
    private Map otherAttrs;

    public OverlayJellyContext(Map caseAttrs,
                               Map initialAttrs)
    {
        this.caseAttrs  = Collections.unmodifiableMap( caseAttrs );
        this.otherAttrs = new HashMap( initialAttrs );
    }

    public void setVariable(String id,
                            Object value)
    {
        this.otherAttrs.put( id,
                             value );
    }

    public Object getVariable(String id)
    {
        if ( this.otherAttrs.containsKey( id ) )
        {
            return this.otherAttrs.get( id );
        }

        return this.caseAttrs.get( id );
    }

    public Object findVariable(String id)
    {
        return getVariable( id );
    }

    public Map getOtherAttributes()
    {
        return this.otherAttrs;
    }
}
