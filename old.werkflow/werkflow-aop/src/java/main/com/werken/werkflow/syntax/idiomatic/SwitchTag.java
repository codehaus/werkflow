package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.idiomatic.SwitchSegment;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class SwitchTag
    extends ComplexActionTagSupport
{
    public SwitchTag()
    {

    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {

        pushSegment( new SwitchSegment() );

        invokeBody( output );

        popSegment();

    }
}
