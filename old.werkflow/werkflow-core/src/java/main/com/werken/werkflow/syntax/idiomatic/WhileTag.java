package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.idiomatic.Segment;
import com.werken.werkflow.definition.idiomatic.WhileSegment;
import com.werken.werkflow.semantics.jelly.JellyExpression;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.expression.Expression;

public class WhileTag
    extends ComplexActionTagSupport
{
    private Expression condition;
    private Segment bodySegment;

    public WhileTag()
    {

    }

    public void setCondition(Expression condition)
    {
        this.condition = condition;
    }

    public Expression getCondition()
    {
        return this.condition;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {

        pushSegment( new WhileSegment( new JellyExpression( getCondition() ) ) );

        invokeBody( output );

        popSegment();
    }
}
