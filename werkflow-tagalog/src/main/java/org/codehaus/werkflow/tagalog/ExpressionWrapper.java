/*
 * $Id$
 */

package org.codehaus.werkflow.tagalog;

import org.codehaus.tagalog.el.Expression;
import org.codehaus.tagalog.el.ExpressionEvaluationException;
import org.codehaus.tagalog.el.ExpressionParseException;
import org.codehaus.tagalog.el.ExpressionParser;
import org.codehaus.tagalog.el.ognl.OgnlExpressionParser;
import org.codehaus.werkflow.Context;

/**
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision$
 */
public class ExpressionWrapper
    implements org.codehaus.werkflow.spi.Expression
{
    private final Expression tagalogExpression;

    private ExpressionWrapper(Expression tagalogExpression)
    {
        this.tagalogExpression = tagalogExpression;
    }

    public Object evaluate(Context context)
    {
        try
        {
            return tagalogExpression.evaluate( context.getContextMap() );
        }
        catch (ExpressionEvaluationException e)
        {
            return null;
        }
    }

    public boolean evaluateAsBoolean(Context context)
    {
        Object result = evaluate( context );

        if ( result instanceof Boolean )
        {
            return ((Boolean) result).booleanValue();
        }

        return false;
    }

    private static ExpressionParser parser = new OgnlExpressionParser();

    public static ExpressionWrapper newExpression(String text)
        throws ExpressionParseException
    {
        return new ExpressionWrapper( parser.parse( text ) );
    }
}
