package com.werken.werkflow.semantics.jelly;

import com.werken.werkflow.ProcessCase;

import org.apache.commons.jelly.JellyContext;

public class CaseJellyContext
    extends JellyContext
{
    private ProcessCase processCase;

    public CaseJellyContext(ProcessCase processCase)
    {
        this.processCase = processCase;
    }

    public ProcessCase getProcessCase()
    {
        return this.processCase;
    }

    public void setVariable(String key,
                            Object value)
    {
        super.setVariable( key,
                           value );
    }

    public Object getVariable(String key)
    {
        if ( getProcessCase().hasAttribute( key ) )
        {
            return getProcessCase().getAttribute( key );
        }

        return super.getVariable( key );
    }

    public Object findVariable(String key)
    {
        if ( getProcessCase().hasAttribute( key ) )
        {
            return getProcessCase().getAttribute( key );
        }

        return super.findVariable( key );
    }

    public void removeVariable(String key)
    {
        // intentionally left blank
    }
}
