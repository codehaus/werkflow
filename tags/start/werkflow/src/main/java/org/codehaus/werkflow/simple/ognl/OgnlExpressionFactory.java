package org.codehaus.werkflow.simple.ognl;

import org.codehaus.werkflow.simple.ExpressionFactory;
import org.codehaus.werkflow.spi.Expression;

public class OgnlExpressionFactory
    implements ExpressionFactory
{
    public Expression newExpression(String text)
        throws Exception
    {
        return new OgnlExpression( text );
    }
}
