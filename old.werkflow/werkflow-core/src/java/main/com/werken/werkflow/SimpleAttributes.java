package com.werken.werkflow;

import java.util.Map;
import java.util.HashMap;

public class SimpleAttributes
implements Attributes
{
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private Map attrs;

    public SimpleAttributes()
    {
        this.attrs = new HashMap();
    }

    public void setAttribute(String key,
                             Object value)
    {
        this.attrs.put( key,
                        value );
    }

    public Object getAttribute(String key)
    {
        return this.attrs.get( key );
    }

    public String[] getAttributeNames()
    {
        return (String[]) this.attrs.keySet().toArray( EMPTY_STRING_ARRAY );
    }
}
