package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.idiomatic.PickSegment;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class PickTag
    extends ComplexActionTagSupport
{
    public PickTag()
    {

    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {

        pushSegment( new PickSegment() );

        invokeBody( output );

        popSegment();

    }
}
