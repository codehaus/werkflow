package com.werken.werkflow.expr;

public abstract class AbstractExpression
    implements Expression
{
    public AbstractExpression()
    {
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
