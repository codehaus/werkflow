package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.idiomatic.Segment;
import com.werken.werkflow.definition.idiomatic.UnsupportedIdiomException;
import com.werken.werkflow.jelly.MiscTagSupport;

import org.apache.commons.jelly.JellyTagException;


public abstract class IdiomaticTagSupport
    extends MiscTagSupport
    implements SegmentReceptor
{
    private Segment segment;

    public IdiomaticTagSupport()
    {

    }

    public void receiveSegment(Segment segment)
        throws JellyTagException
    {
        try
        {
            peekSegment().addSegment( segment );
        }
        catch (UnsupportedIdiomException e)
        {
            throw new JellyTagException( e );
        }
    }

    protected Segment peekSegment()
    {
        return this.segment;
    }

    protected void pushSegment(Segment segment)
    {
        this.segment = segment;
    }

    protected void popSegment()
        throws JellyTagException
    {
        if ( this instanceof DefinitionRoot )
        {
            this.segment = null;
            return;
        }

        SegmentReceptor tag = (SegmentReceptor) findAncestorWithClass( SegmentReceptor.class );

        if ( tag == null )
        {
            throw new JellyTagException( "invalid context");
        }

        tag.receiveSegment( this.segment );

        this.segment = null;
    }

    protected MessageType getMessageType(String id)
    {
        return null;
    }
}
