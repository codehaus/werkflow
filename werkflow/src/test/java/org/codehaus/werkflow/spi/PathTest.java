package org.codehaus.werkflow.spi;

public class PathTest
    extends SpiTestBase
{
    public void testDefaultConstructor()
        throws Exception
    {
        Path path = new Path();

        assertLength( "default path has 1 segment",
                      1,
                      path.getSegments() );
    }

    public void testArrayConstructor()
        throws Exception
    {
        Path path = new Path( new int[] { 5, 4, 3, 2, 1 } );

        assertLength( "array path has 5 segments",
                      5,
                      path.getSegments() );

        // --

        assertEquals( "segment 0 is 5",
                      5,
                      path.getSegments()[ 0 ] );

        assertEquals( "segment 1 is 4",
                      4,
                      path.getSegments()[ 1 ] );

        assertEquals( "segment 2 is 3",
                      3,
                      path.getSegments()[ 2 ] );

        assertEquals( "segment 3 is 2",
                      2,
                      path.getSegments()[ 3 ] );

        assertEquals( "segment 4 is 1",
                      1,
                      path.getSegments()[ 4 ] );
    }

    public void testChildPath()
        throws Exception
    {
        Path path = new Path();

        Path child = path.childPath( 42 );

        Path grandChild = child.childPath( 84 );

        assertTrue( "child has path.length+1 segments",
                    ( path.getSegments().length+1 ) == child.getSegments().length );

        assertTrue( "grandChild has child.length+1 segments",
                    ( child.getSegments().length+1 ) == grandChild.getSegments().length );

        for ( int i = 0 ; i < path.getSegments().length ; ++i )
        {
            if ( path.getSegments()[ i ] != child.getSegments()[ i ] )
            {
                fail( "child segment <" + i + "> should have been <" + path.getSegments()[ i ] + "> but was <" + child.getSegments()[ i ] + ">" );
            }
        }

        assertEquals( "child last segment should be 42",
                      42,
                      child.getSegments()[ child.getSegments().length - 1 ] );

        for ( int i = 0 ; i < child.getSegments().length ; ++i )
        {
            if ( child.getSegments()[ i ] != grandChild.getSegments()[ i ] )
            {
                fail( "grandChild segment <" + i + "> should have been <" + child.getSegments()[ i ] + "> but was <" + grandChild.getSegments()[ i ] + ">" );
            }
        }

        assertEquals( "grandChild last segment should be 84",
                      84,
                      grandChild.getSegments()[ grandChild.getSegments().length - 1 ] );

    }

    public void testParentPath()
        throws Exception
    {
        Path path = new Path();

        Path child = path.childPath( 42 );

        Path grandChild = child.childPath( 84 );

        assertEquals( "child's parent is path",
                      path,
                      child.parentPath() );

        assertEquals( "grandChild's parent is child",
                      child,
                      grandChild.parentPath() );
    }

    public void testSiblingPath()
        throws Exception
    {
        Path path = new Path();

        Path child = path.childPath( 42 );

        Path sibling = child.nextSiblingPath();

        for ( int i = 0 ; i < path.getSegments().length ; ++i )
        {
            if ( child.getSegments()[ i ] != path.getSegments()[ i ] )
            {
                fail( "child segment <" + i + "> should have been <" + path.getSegments()[ i ] + "> but was <" + child.getSegments()[ i ] + ">" );
            }
        }

        assertEquals( "child last segment should be 42",
                      42,
                      child.getSegments()[ child.getSegments().length - 1 ] );

        for ( int i = 0 ; i < path.getSegments().length ; ++i )
        {
            if ( sibling.getSegments()[ i ] != path.getSegments()[ i ] )
            {
                fail( "sibling segment <" + i + "> should have been <" + path.getSegments()[ i ] + "> but was <" + sibling.getSegments()[ i ] + ">" );
            }
        }

        assertEquals( "sibling last segment should be 43",
                      43,
                      sibling.getSegments()[ sibling.getSegments().length - 1 ] );
    }

    public void testHeadAndTail()
        throws Exception
    {
        Path path = new Path().childPath( 42 ).childPath( 84 ).childPath( 21 );

        assertLength( "head is 3 segments long",
                      3,
                      path.head() );

        assertEquals( "head segment 0 is 0",
                      0,
                      path.head()[ 0 ] );

        assertEquals( "head segment 1 is 42",
                      42,
                      path.head()[ 1 ] );

        assertEquals( "head segment 2 is 84",
                      84,
                      path.head()[ 2 ] );

        assertEquals( "tail is 21",
                      21,
                      path.tail() );
    }
}
