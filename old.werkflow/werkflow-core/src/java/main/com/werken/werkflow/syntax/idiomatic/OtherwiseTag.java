package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.idiomatic.Segment;
import com.werken.werkflow.definition.idiomatic.ConditionalSegment;
import com.werken.werkflow.semantics.jelly.JellyExpression;

import org.apache.commons.jelly.expression.Expression;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class OtherwiseTag
    extends IdiomaticTagSupport
{
    public OtherwiseTag()
    {

    }

    public void receiveSegment(Segment segment)
        throws JellyTagException
    {
        SwitchTag tag = (SwitchTag) findAncestorWithClass( SwitchTag.class );

        if ( tag == null )
        {
            throw new JellyTagException( "not within switch" );
        }

        tag.receiveSegment( segment );
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        invokeBody( output );
    }
}
