package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.idiomatic.TerminationSegment;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class TerminateTag
    extends ComplexActionTagSupport
{
    public TerminateTag()
    {

    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        pushSegment( new TerminationSegment() );

        invokeBody( output );

        popSegment();

    }
}
