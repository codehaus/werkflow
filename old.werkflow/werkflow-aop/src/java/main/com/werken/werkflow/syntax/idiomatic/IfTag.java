package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.idiomatic.Segment;
import com.werken.werkflow.definition.idiomatic.IfSegment;
import com.werken.werkflow.definition.idiomatic.SequenceSegment;
import com.werken.werkflow.semantics.jelly.JellyExpression;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.expression.Expression;

public class IfTag
    extends ComplexActionTagSupport
{
    private Expression condition;

    public IfTag()
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

        requireObjectAttribute( "condition",
                                getCondition() );

        pushSegment( new IfSegment( new JellyExpression( getCondition() ) ) );

        invokeBody( output );

        popSegment();
    }
}
