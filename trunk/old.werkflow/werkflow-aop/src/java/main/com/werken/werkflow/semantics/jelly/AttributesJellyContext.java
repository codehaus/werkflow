package com.werken.werkflow.semantics.jelly;

import com.werken.werkflow.Attributes;

import org.apache.commons.jelly.JellyContext;

public class AttributesJellyContext
    extends JellyContext
{
    public AttributesJellyContext(Attributes attrs)
    {
        String[] attrNames = attrs.getAttributeNames();

        for ( int i = 0 ; i < attrNames.length ; ++i )
        {
            setVariable( attrNames[i],
                         attrs.getAttribute( attrNames[i] ) );
        }
    }
}

