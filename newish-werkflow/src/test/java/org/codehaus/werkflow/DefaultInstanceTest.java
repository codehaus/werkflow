package org.codehaus.werkflow;

public class DefaultInstanceTest
    extends WerkflowTestBase
{
    public void testConstructor()
        throws Exception
    {
        DefaultInstance instance = new DefaultInstance( null,
                                                        null,
                                                        "ted" );

        assertEquals( "instance name is 'ted'",
                      "ted",
                      instance.getId() );
    }

    public void testPushPop()
        throws Exception
    {
        DefaultInstance instance = new DefaultInstance( null,
                                                        null,
                                                        "ted" );

        Path path_0 = new Path();

        Path path_00 = path_0.childPath( 0 );

        Path path_01 = path_0.childPath( 1 );
        
        Path path_017 = path_01.childPath( 7 );

        instance.push( path_0 );
        instance.push( path_00 );
        instance.push( path_01 );
        instance.push( path_017 );

        assertLength( "path_0 has 2 active children",
                      2,
                      instance.getActiveChildren( path_0 ) );

        assertContains( "path_0 has path_00 as active child",
                        path_00,
                        instance.getActiveChildren( path_0 ) );

        assertContains( "path_0 has path_01 as active child",
                        path_01,
                        instance.getActiveChildren( path_0 ) );

        assertEmpty( "path_00 has no active children",
                     instance.getActiveChildren( path_00 ) );

        assertLength( "path_01 has 1 active child",
                      1,
                      instance.getActiveChildren( path_01 ) );

        assertContains( "path_01 has path_017 as active child",
                        path_017,
                        instance.getActiveChildren( path_01 ) );
    }

    public void testPushPopAssumptionViolation()
        throws Exception
    {
        DefaultInstance instance = new DefaultInstance( null,
                                                        null,
                                                        "ted" );

        Path path = new Path();

        Path child = path.childPath( 42 );

        instance.push( path );
        instance.push( child );

        try
        {
            instance.push( child );
            fail( "should have thrown AssumptionViolationError" );
        }
        catch (AssumptionViolationError e)
        {
            // expected and correct
        }

        instance.pop( child );

        try
        {
            instance.pop( child );
            fail( "should have thrown AssumptionViolationError" );
        }
        catch (AssumptionViolationError r)
        {
            // expected and correct
        }

        Path grandChild = child.childPath( 84 );

        try
        {
            instance.push( grandChild );
            fail( "should have thrown AssumptionViolationError" );
        }
        catch (AssumptionViolationError e)
        {
            // expected and correct
        }

        instance.push( child );
        instance.push( grandChild );

        try
        {
            instance.pop( child );
            fail( "should have thrown AssumptionViolation" );
        }
        catch (AssumptionViolationError e)
        {
            // expected and correct;
        }

        instance.pop( grandChild );
        instance.pop( child );
    }
}
