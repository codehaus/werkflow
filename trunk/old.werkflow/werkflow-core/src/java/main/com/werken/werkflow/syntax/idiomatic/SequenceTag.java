package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.idiomatic.SequenceSegment;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class SequenceTag
    extends ComplexActionTagSupport
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
