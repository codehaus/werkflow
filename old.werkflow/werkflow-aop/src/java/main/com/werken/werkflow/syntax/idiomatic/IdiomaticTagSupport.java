package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.MessageType;
import com.werken.werkflow.definition.idiomatic.Segment;
import com.werken.werkflow.definition.idiomatic.UnsupportedIdiomException;
import com.werken.werkflow.syntax.fundamental.FundamentalTagSupport;

import org.apache.commons.jelly.JellyTagException;

import java.util.Stack;

public abstract class IdiomaticTagSupport
    extends FundamentalTagSupport
{
    public static final String SEGMENT_STACK_KEY = "werkflow.segment.stack";

    private Segment segment;

    public IdiomaticTagSupport()
    {

    }

    protected Stack getSegmentStack()
    {
        return (Stack) getContext().getVariable( SEGMENT_STACK_KEY );
    }

    protected void setSegmentStack(Stack stack)
    {
        getContext().setVariable( SEGMENT_STACK_KEY,
                                  stack );
    }

    protected Segment peekSegment()
    {
        return (Segment) getSegmentStack().peek();
    }

    protected void pushSegment(Segment segment)
    {
        Stack stack = getSegmentStack();

        if ( stack == null )
        {
            stack = new Stack();
            setSegmentStack( stack );
        }
        
        stack.push( segment );
    }

    protected void popSegment()
        throws JellyTagException
    {
        Stack stack = getSegmentStack();

        Segment segment = (Segment) stack.pop();

        if ( ! stack.isEmpty() )
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
    }
}
