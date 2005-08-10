package org.codehaus.werkflow.helpers;

import org.codehaus.werkflow.simple.ExpressionFactory;
import org.codehaus.werkflow.spi.Expression;
import org.codehaus.werkflow.expressions.False;

/*
 * $Id$
 */
public class MockExpressionFactory implements ExpressionFactory {

    public Expression newExpression( String text ) throws Exception {

        return False.INSTANCE;

    }

}
