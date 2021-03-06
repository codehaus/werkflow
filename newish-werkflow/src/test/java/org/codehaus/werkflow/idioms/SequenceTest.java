package org.codehaus.werkflow.idioms;

import org.codehaus.werkflow.*;
import org.codehaus.werkflow.spi.*;
import org.codehaus.werkflow.idioms.*;

public class SequenceTest
    extends WerkflowTestBase
{
    public void testBeginEnd()
        throws Exception
    {
        DefaultInstance instance = new DefaultInstance( new Workflow( "larry" ),
                                                        "ted",
                                                        new InitialContext() );

        Sequence sequence = new Sequence();

        MockSyncComponent child0 = new MockSyncComponent();
        MockSyncComponent child1 = new MockSyncComponent();
        MockSyncComponent child2 = new MockSyncComponent();

        sequence.addStep( child0 );
        sequence.addStep( child1 );
        sequence.addStep( child2 );

        Path sequencePath = new Path();

        Path[] beginPaths = null;
        Path   endPath    = null;

        beginPaths = sequence.begin( instance,
                                     sequencePath );

        assertLength( "begin returns 1 path",
                      1,
                      beginPaths );

        assertEquals( "begin returns child0",
                      sequencePath.childPath( 0 ),
                      beginPaths[ 0 ] );

        endPath = sequence.endChild( instance,
                                     sequencePath.childPath( 0 ) );

        assertEquals( "end path is child1",
                      sequencePath.childPath( 1 ),
                      endPath );

        endPath = sequence.endChild( instance,
                                     endPath );

        assertEquals( "end path is child2",
                      sequencePath.childPath( 2 ),
                      endPath );

        endPath = sequence.endChild( instance,
                                     endPath );

        assertSame( "end path is NONE",
                    AsyncComponent.NONE,
                    endPath );
    }
}
