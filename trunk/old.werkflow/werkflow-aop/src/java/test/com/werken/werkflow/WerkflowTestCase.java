package com.werken.werkflow;

import junit.framework.TestCase;

import java.util.Arrays;

public class WerkflowTestCase
    extends TestCase
{
    public static class Relay
    {
        private Object value;

        public Relay()
        {

        }

        public Relay(Object value)
        {
            this.value = value;
        }

        public Object getValue()
        {
            return value;
        }

        public void setValue(Object value)
        {
            this.value = value;
        }
    }

    protected void assertContains(Object obj,
                                  Object[] array)
    {
        for ( int i = 0 ; i < array.length ; ++i )
        {
            if ( array[i].equals( obj ) )
            {
                return;
            }
        }

        fail( obj + " not in " + Arrays.asList( array ) );
    }

    protected void assertContains(Object[] expected,
                                  Object[] actual)
    {
        for ( int i = 0 ; i < expected.length ; ++i )
        {
            assertContains( expected[i],
                            actual );
        }
    }

    protected void assertEmpty(Object[] array)
    {
        assertEquals( 0,
                      array.length );
    }

    protected void assertLength(int len,
                                Object[] array)
    {
        assertEquals( len,
                      array.length );
    }

    protected void dumpArray(String message,
                             Object[] array)
    {
        System.err.println( message + ": " + Arrays.asList( array ) );
    }

}
