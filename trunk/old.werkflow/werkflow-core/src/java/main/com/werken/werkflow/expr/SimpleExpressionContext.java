package com.werken.werkflow.expr;

import java.util.Map;
import java.util.HashMap;

public class SimpleExpressionContext
    implements ExpressionContext
{
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private Map values;

    public SimpleExpressionContext()
    {
        this.values = new HashMap();
    }

    public String[] getNames()
    {
        return (String[]) this.values.keySet().toArray( EMPTY_STRING_ARRAY );
    }

    public void setValue(String name,
                         Object value)
    {
        this.values.put( name,
                         value );
    }

    public Object getValue(String name)
    {
        return this.values.get( name );
    }
}
