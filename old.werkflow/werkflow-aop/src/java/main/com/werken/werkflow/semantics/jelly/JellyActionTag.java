package com.werken.werkflow.semantics.jelly;

import com.werken.werkflow.definition.fundamental.AbstractActionTag;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class JellyActionTag
    extends AbstractActionTag
{
    public JellyActionTag()
    {

    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        JellyAction action = new JellyAction( getBody() );

        setAction( action );
    }
}
