package com.werken.werkflow.syntax;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.impl.DynamicTagLibrary;

public class TagTag
    extends org.apache.commons.jelly.tags.define.TagTag
{
    public TagTag()
    {
    }

    protected DynamicTagLibrary getTagLibrary()
        throws JellyTagException
    {
        Syntax syntax = (Syntax) getContext().getVariable( Syntax.class.getName() );

        if ( syntax == null )
        {
            throw new JellyTagException( "not within syntax context" );
        }

        return syntax.getTagLibrary();
    }
}
