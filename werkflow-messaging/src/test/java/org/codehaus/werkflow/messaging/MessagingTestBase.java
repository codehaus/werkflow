package org.codehaus.werkflow.messaging;

import junit.framework.TestCase;

import java.util.Arrays;

public class MessagingTestBase
    extends TestCase
{
    protected void setUp()
        throws Exception
    {
        super.setUp();
    }

    protected void tearDown()
        throws Exception
    {
        super.tearDown();
    }

    public void assertLength(int expected,
                             Object[] array)
    {
        if ( array.length == expected )
        {
            return;
        }

        fail( "expected array of length <" + expected + "> but found of length <" + array.length + "> :: " + Arrays.asList( array ) );
    }

    public void assertContains(Object expected,
                               Object[] actual)
    {
        for ( int i = 0 ; i < actual.length ; ++i )
        {
            if ( actual[ i ].equals( expected ) )
            {
                return;
            }
        }

        fail( "<" + expected + "> not found in <" + Arrays.asList( actual ) + ">" );
    }
}
