package com.werken.werkflow.syntax.bpel4ws;

import com.werken.werkflow.definition.idiomatic.EmptySegment;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class EmptyTag
    extends ActionTagSupport
{
    public EmptyTag()
    {

    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        pushSegment( new EmptySegment() );

        invokeBody( output );

        popSegment();
    }
}
