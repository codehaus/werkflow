package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.idiomatic.SequenceSegment;
import com.werken.werkflow.semantics.jelly.JellyExpression;

import org.apache.commons.jelly.expression.Expression;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class OtherwiseTag
    extends ComplexActionTagSupport
{
    public OtherwiseTag()
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
