package com.werken.werkflow.personality.extension;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;

import com.werken.werkflow.syntax.fundamental.AbstractActionTag;

/**
 * Sample werkflow action tag.
 *
 * @author Mark Wilkinson
 */
public class FooTag extends AbstractActionTag
{
    private String value;

    public void setValue(String value)
    {
        this.value = value;
    }

    public void doTag(XMLOutput output)
        throws MissingAttributeException, JellyTagException
    {
        setAction( new FooAction( value ) );
    }
}
