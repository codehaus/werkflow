package org.codehaus.werkflow;

public class ScopeTest
    extends WerkflowTestBase
{
    public void testNoParentConstructor()
        throws Exception
    {
        Scope scope = new Scope( 42 );

        assertEquals( "segment is 42",
                      42,
                      scope.getSegment() );

        assertLength( "no children",
                      0,
                      scope.getChildren() );
    }

    public void testChildManipulation()
        throws Exception
    {
        Scope scope = new Scope( 42 );

        Scope child = scope.newChild( 84 );

        assertEquals( "scope segment is 42",
                      42,
                      scope.getSegment() );

        assertEquals( "child segment is 84",
                      84,
                      child.getSegment() );

        assertLength( "scope has 1 child",
                      1,
                      scope.getChildren() );
        
        assertSame( "child scope is child of scope",
                    child,
                    scope.getChildren()[ 0 ] );

        assertSame( "scope's child #84 is child",
                    child,
                    scope.getChild( 84 ) );

        // --

        scope.removeChild( 84 );


        assertEmpty( "scope has no children",
                     scope.getChildren() );

                      
    }
}
