package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.idiomatic.Segment;
import com.werken.werkflow.definition.idiomatic.ConditionalSegment;
import com.werken.werkflow.semantics.jelly.JellyExpression;

import org.apache.commons.jelly.expression.Expression;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class CaseTag
    extends IdiomaticTagSupport
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

    public void receiveSegment(Segment segment)
        throws JellyTagException
    {
        SwitchTag tag = (SwitchTag) findAncestorWithClass( SwitchTag.class );

        if ( tag == null )
        {
            throw new JellyTagException( "not within switch" );
        }

        ConditionalSegment caseSegment = new ConditionalSegment( new JellyExpression( getCondition() ),
                                                                 segment );

        tag.receiveSegment( caseSegment );
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireObjectAttribute( "condition",
                                getCondition() );

        invokeBody( output );
    }
}
