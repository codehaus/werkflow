package com.werken.werkflow.semantics.jelly;

import com.werken.werkflow.ProcessCase;
import com.werken.werkflow.definition.MessageCorrelator;

import org.apache.commons.jelly.expression.Expression;

public class JellyMessageCorrelator
    implements MessageCorrelator
{
    private String messageId;
    private Expression expression;

    public JellyMessageCorrelator(String messageId,
                                  Expression expression)
    {
        this.messageId = messageId;
        this.expression = expression;
    }

    public String getMessageId()
    {
        return this.messageId;
    }

    public Expression getExpression()
    {
        return this.expression;
    }

    public boolean correlates(Object message,
                              ProcessCase processCase)
        throws Exception
    {
        CaseJellyContext context = new CaseJellyContext( processCase );

        context.setVariable( getMessageId(),
                             message );

        return getExpression().evaluateAsBoolean( context );
    }
}
