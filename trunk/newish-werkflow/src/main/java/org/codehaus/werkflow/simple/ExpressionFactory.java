package org.codehaus.werkflow.simple;

import org.codehaus.werkflow.spi.Expression;

public interface ExpressionFactory
{
    Expression newExpression(String text)
        throws Exception;
}
