package com.werken.werkflow.syntax.bpel4ws;

import com.werken.werkflow.definition.idiomatic.WhileSegment;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class WhileTag
    extends ActionTagSupport
{
    private String condition;

    public WhileTag()
    {

    }

    public void setCondition(String condition)
    {
        this.condition = condition;
    }

    public String getCondition()
    {
        return this.condition;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        // pushSegment( new WhileSegment( getExpression() ) );
        pushSegment( new WhileSegment( null ) );

        invokeBody( output );

        popSegment();
    }
}
