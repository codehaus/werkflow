package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.idiomatic.ParallelSegment;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class ParallelTag
    extends ComplexActionTagSupport
{
    public ParallelTag()
    {

    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        pushSegment( new ParallelSegment() );

        invokeBody( output );

        popSegment();
    }
}
