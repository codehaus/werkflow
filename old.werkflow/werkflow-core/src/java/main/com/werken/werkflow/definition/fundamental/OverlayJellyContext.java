package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.ProcessCase;

import org.apache.commons.jelly.JellyContext;

import java.util.Map;
import java.util.HashMap;

public class OverlayJellyContext
    extends JellyContext
{
    private ProcessCase processCase;
    private Map attrs;

    public OverlayJellyContext(ProcessCase processCase,
                               Map initialAttrs)
    {
        this.processCase = processCase;
        this.attrs       = new HashMap( initialAttrs );
    }

    public ProcessCase getProcessCase()
    {
        return this.processCase;
    }

    public void setVariable(String id,
                            Object value)
    {
        this.attrs.put( id,
                        value );
    }

    public Object getVariable(String id)
    {
        if ( this.attrs.containsKey( id ) )
        {
            return this.attrs.get( id );
        }

        return getProcessCase().getAttribute( id );
    }

    public Object findVariable(String id)
    {
        return getVariable( id );
    }

    public Map getAttributes()
    {
        return this.attrs;
    }
}
