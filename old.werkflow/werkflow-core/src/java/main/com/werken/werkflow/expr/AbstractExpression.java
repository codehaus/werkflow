package com.werken.werkflow.expr;

public abstract class AbstractExpression
    implements Expression
{
    public AbstractExpression()
    {
    }

    public boolean evaluateAsBoolean(ExpressionContext context)
        throws Exception
    {
        return asBoolean( evaluate( context ) );
    }

    public boolean asBoolean(Object value)
    {
        if ( value instanceof Boolean )
        {
            return ((Boolean)value).booleanValue();
        }

        if ( value instanceof Integer )
        {
            return ((Integer)value).intValue() != 0;
        }

        if ( value instanceof Long )
        {
            return ((Long)value).longValue() != 0;
        }

        if ( value instanceof String )
        {
            return stringAsBoolean( (String) value );
        }

        return false;
    }

    public boolean stringAsBoolean(String str)
    {
        if ( str.equals( "true" )
             ||
             str.equals( "on" )
             ||
             str.equals( "1" )
             ||
             str.equals( "yes" ) )
        {
            return true;
        }

        return false;
    }
}
