package com.werken.werkflow.example;

import java.lang.reflect.Method;

public class Example
{
    public static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    public static void main(String[] args)
        throws Exception
    {
        if ( args.length < 1
             ||
             args.length > 2 )
        {
            System.err.println( "usage: java com.werken.werkflow.example.Example <exPkg> [<exName>]" );
                return;
        }

        String exClassName = "com.werken.werkflow.example." + args[0] + ".Example";

        Class exClass = Class.forName( exClassName );

        Object example = exClass.newInstance();

        String exName = "defaultExample";

        if ( args.length == 2 )
        {
            exName = args[1] + "Example";
        }

        Method method = exClass.getMethod( exName,
                                           EMPTY_CLASS_ARRAY );

        method.invoke( example,
                       EMPTY_OBJECT_ARRAY );
    }
}
