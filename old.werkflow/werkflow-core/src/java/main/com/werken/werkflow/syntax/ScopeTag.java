package com.werken.werkflow.syntax;

import com.werken.werkflow.jelly.MiscTagSupport;
import com.werken.werkflow.definition.Scope;
import com.werken.werkflow.syntax.fundamental.FundamentalTagSupport;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class ScopeTag
    extends FundamentalTagSupport
{
    public ScopeTag()
    {

    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        pushScope();
        invokeBody( output );
        popScope();
    }

}
