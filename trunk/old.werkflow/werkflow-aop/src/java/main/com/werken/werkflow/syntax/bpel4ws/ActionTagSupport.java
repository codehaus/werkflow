package com.werken.werkflow.syntax.bpel4ws;

import com.werken.werkflow.definition.idiomatic.Segment;
import com.werken.werkflow.definition.idiomatic.UnsupportedIdiomException;

import org.apache.commons.jelly.JellyTagException;

public abstract class ActionTagSupport
    extends Bpel4wsTagSupport
    implements SegmentReceptor
{
    private Segment segment;

    public ActionTagSupport()
    {

    }

    public void receiveSegment(Segment segment)
        throws UnsupportedIdiomException
    {
        peekSegment().addSegment( segment );
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
        SegmentReceptor tag = (SegmentReceptor) findAncestorWithClass( SegmentReceptor.class );

        if ( tag == null )
        {
            throw new JellyTagException( "invalid context");
        }

        try
        {
            tag.receiveSegment( this.segment );
        }
        catch (UnsupportedIdiomException e)
        {
            throw new JellyTagException( e );
        }

        this.segment = null;
    }
}
