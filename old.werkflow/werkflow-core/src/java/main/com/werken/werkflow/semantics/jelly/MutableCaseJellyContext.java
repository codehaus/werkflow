package com.werken.werkflow.semantics.jelly;

import com.werken.werkflow.action.MutableProcessCase;

import org.apache.commons.jelly.JellyContext;

public class MutableCaseJellyContext
    extends JellyContext
{
    private MutableProcessCase processCase;

    public MutableCaseJellyContext(MutableProcessCase processCase)
    {
        this.processCase = processCase;
    }

    public MutableProcessCase getProcessCase()
    {
        return this.processCase;
    }

    public void setVariable(String key,
                            Object value)
    {
        getProcessCase().setAttribute( key,
                                       value );
    }

    public Object getVariable(String key)
    {
        return getProcessCase().getAttribute( key );
    }

    public Object findVariable(String key)
    {
        return getVariable( key );
    }

    public void removeVariable(String key)
    {
        getProcessCase().clearAttribute( key );
    }
}
