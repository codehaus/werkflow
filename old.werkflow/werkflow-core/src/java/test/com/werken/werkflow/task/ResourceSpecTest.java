package com.werken.werkflow.task;

import junit.framework.TestCase;

import java.util.Arrays;

public class ResourceSpecTest
    extends TestCase
{
    public void testGeneral()
    {
        ResourceSpec spec = new ResourceSpec();

        assertEquals( 0,
                      spec.getResourceClassIds().length );

        spec.addResourceClassId( "foo" );
        spec.addResourceClassId( "foo" );
        spec.addResourceClassId( "foo" );
        spec.addResourceClassId( "foo" );

        assertEquals( 1,
                      spec.getResourceClassIds().length );

        assertContains( "foo",
                        spec.getResourceClassIds() );

        spec.addResourceClassId( "bar" );

        assertEquals( 2,
                      spec.getResourceClassIds().length );

        assertContains( "foo",
                        spec.getResourceClassIds() );

        assertContains( "bar",
                        spec.getResourceClassIds() );
    }

    void assertContains(Object obj,
                        Object[] array)
    {
        for ( int i = 0 ; i < array.length; ++i )
        {
            if ( array[i].equals( obj ) )
            {
                return;
            }
        }

        fail( obj + " not in " + Arrays.asList( array ) );
    }
}
