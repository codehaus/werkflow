package org.codehaus.werkflow;

import junit.framework.TestCase;

import java.util.Collection;
import java.util.Arrays;

public class WerkflowTestBase
    extends TestCase
{
    public void assertLength(String message,
                             int expected,
                             Object[] array)
    {
        if ( array == null )
        {
            fail( "array is null" );
        }

        if ( array.length != expected )
        {
            fail( message + ": array length <" + expected +"> expected, but was <" + array.length + ">" );
        }
    }

    public void assertLength(String message,
                             int expected,
                             int[] array)
    {
        if ( array == null )
        {
            fail( "array is null" );
        }

        if ( array.length != expected )
        {
            fail( message + ": array length <" + expected +"> expected, but was <" + array.length + ">" );
        }
    }

    public void assertLength(String message,
                             int expected,
                             Collection collection)
    {
        if ( collection == null )
        {
            fail( "collection is null" );
        }

        if ( collection.size() != expected )
        {
            fail( message + ": collection length <" + expected +"> expected, but was <" + collection.size() + ">" );
        }
    }

    public void assertEmpty(String message,
                            Object[] array)
    {
        if ( array == null )
        {
            fail( "array is null" );
        }

        if ( array.length != 0 )
        {
            fail( message + ": array not empty <" + Arrays.asList( array ) + ">" );
        }
    }

    public void assertContains(String message,
                               Object expected,
                               Object[] array)
    {
        for ( int i = 0 ; i < array.length ; ++i )
        {
            if ( array[ i ].equals( expected ) )
            {
                return;
            }
        }

        fail( "object <" + expected + "> not in array <" + Arrays.asList( array ) + ">" );
    }

    public void assertContains(String message,
                               Object expected,
                               Collection collection)
    {
        if ( collection.contains( expected ) )
        {
            return;
        }

        fail( "object <" + expected + "> not in collection <" + collection + ">" );
    }
}
