package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.idiomatic.Segment;
import com.werken.werkflow.definition.idiomatic.ConditionalSegment;
import com.werken.werkflow.semantics.jelly.JellyExpression;
import com.werken.werkflow.syntax.fundamental.ActionReceptor;

import org.apache.commons.jelly.expression.Expression;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class CaseTag
    extends ComplexActionTagSupport
{
    private Expression condition;

    public CaseTag()
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

        pushSegment( new ConditionalSegment( new JellyExpression( getCondition() ) ) );

        invokeBody( output );

        popSegment();
    }
}
