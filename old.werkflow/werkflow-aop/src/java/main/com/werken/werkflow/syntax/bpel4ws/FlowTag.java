package com.werken.werkflow.syntax.bpel4ws;

import com.werken.werkflow.definition.idiomatic.ParallelSegment;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class FlowTag
    extends ActionTagSupport
{
    public FlowTag()
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
