package org.codehaus.werkflow.simple;

import org.codehaus.werkflow.spi.Expression;
import org.codehaus.werkflow.expressions.False;

public class MockExpressionFactory
    implements ExpressionFactory
{
    public MockExpressionFactory()
    {

    }

    public Expression newExpression(String text)
        throws Exception
    {
        return False.INSTANCE;
    }
}
