package com.werken.werkflow.syntax.bpel4ws;

import com.werken.werkflow.definition.idiomatic.SequenceSegment;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class SequenceTag
    extends ActionTagSupport
{
    public SequenceTag()
    {

    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        pushSegment( new SequenceSegment() );

        invokeBody( output );

        popSegment();
    }
}
