package org.codehaus.werkflow.simple;

import org.codehaus.werkflow.Expression;
import org.codehaus.werkflow.False;

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
