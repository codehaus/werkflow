package com.werken.werkflow.jelly;

import org.apache.commons.jelly.Tag;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;

public abstract class MiscTagSupport
    extends TagSupport
{
    public void requireStringAttribute(String attrName,
                                       String attrValue)
        throws MissingAttributeException
    {
        requireObjectAttribute( attrName,
                                attrValue );

        if ( "".equals( attrValue ) )
        {
            throw new MissingAttributeException( attrName );
        }
    }

    public void requireObjectAttribute(String attrName,
                                       Object attrValue)
        throws MissingAttributeException
    {
        if ( attrValue == null )
        {
            throw new MissingAttributeException( attrName );
        }
    }

    public Tag requiredAncestor(String tagName,
                                Class tagClass)
        throws JellyTagException
    {
        Tag ancestor = findAncestorWithClass( tagClass ); 
        
        if ( ancestor == null )
        {
            throw new JellyTagException( tagName + " ancestor not found" );
        }
        
        return ancestor;
    }
}
