package org.codehaus.werkflow.spi;

import java.util.Map;
import java.util.HashMap;

public class DefaultSatisfactionValues
    implements SatisfactionValues
{
    private Map values;

    public DefaultSatisfactionValues()
    {
        this.values = new HashMap();
    }

    public DefaultSatisfactionValues(Map values)
    {
        this();
        this.values.putAll( values );
    }

    public String[] getNames()
    {
        return (String[]) this.values.keySet().toArray( new String[ this.values.size() ]);
    }

    public Object getValue(String name)
    {
        return this.values.get( name );
    }

    public void setValue(String name,
                         Object value)
    {
        this.values.put( name,
                         value );
    }
}
