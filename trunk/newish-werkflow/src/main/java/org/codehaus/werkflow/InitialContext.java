package org.codehaus.werkflow;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.Enumeration;

public class InitialContext
    implements Context
{
    private Map context;

    public InitialContext()
    {
        this.context = new HashMap();
    }

    public InitialContext(Properties props)
    {
        this();

        for ( Enumeration names = props.propertyNames();
              names.hasMoreElements(); )
        {
            String name = (String) names.nextElement();
            String value = props.getProperty( name );

            set( name,
                 value );
        }
    }

    public void set(String name,
                    Object value)
    {
        this.context.put( name,
                          value );
    }

    public Object get(String name)
    {
        return this.context.get( name );
    }

        
}
