package org.codehaus.werkflow;

import org.codehaus.werkflow.spi.*;
import org.codehaus.werkflow.idioms.*;

public class WorkflowTest
    extends WerkflowTestBase
{
    public void testIdOnlyConstructor()
        throws Exception
    {
        Workflow workflow = new Workflow( "ted" );

        assertEquals( "workflow id is 'ted'",
                      "ted",
                      workflow.getId() );

        assertNull( "workflow has no body",
                    workflow.getBody() );

        assertEmpty( "workflow has no child",
                     workflow.getChildren() );
    }

    public void testIdAndBodyConstructor()
        throws Exception
    {
        Component body = new MockAsyncComponent();

        Workflow workflow = new Workflow( "ted",
                                          body );

        assertEquals( "workflow id is 'ted'",
                      "ted",
                      workflow.getId() );

        assertSame( "workflow body is body",
                    body,
                    workflow.getBody() );

        assertLength( "workflow has 1 child",
                      1,
                      workflow.getChildren() );

        assertSame( "workflow child is body",
                    body,
                    workflow.getChildren()[ 0 ] );
    }

    public void testGetComponent()
        throws Exception
    {
        MockAsyncComponent body = new MockAsyncComponent();

        MockAsyncComponent subBody = new MockAsyncComponent();

        body.addChild( subBody );

        Workflow workflow = new Workflow( "ted",
                                          body );

        assertSame( "path 0 is body",
                    body,
                    workflow.getComponent( new Path() ) );

        assertSame( "path 00 is subBody",
                    subBody,
                    workflow.getComponent( new Path().childPath( 0 ) ) );
    }

    public void testGetComponentAssumptionViolation()
        throws Exception
    {
        MockAsyncComponent body = new MockAsyncComponent();

        MockAsyncComponent subBody = new MockAsyncComponent();

        body.addChild( subBody );

        Workflow workflow = new Workflow( "test",
                                          body );

        Path bodyPath    = new Path();
        Path subBodyPath = bodyPath.childPath( 0 );
        Path noComponentPath = subBodyPath.childPath( 1 );

        assertNotNull( workflow.getComponent( bodyPath ) );
        assertNotNull( workflow.getComponent( subBodyPath ) );

        try
        {
            workflow.getComponent( noComponentPath );
            fail( "should have thrown AssumptionViolationError" );
        }
        catch (AssumptionViolationError e)
        {
            // expected and correct
        }

        Path bogusPath = new Path( new int[] { 1 } );

        try
        {
            workflow.getComponent( bogusPath );
            fail( "should have thrown AssumptionViolationError" );
        }
        catch (AssumptionViolationError e)
        {
            // expected and correct
        }

        body.addChild( null );

        Path subBodyPath2 = bodyPath.childPath( 1 );

        try
        {
            workflow.getComponent( subBodyPath2 );
            fail( "should have thrown AssumptionViolationError" );
        }
        catch (AssumptionViolationError e)
        {
            // expected and correct
        }
    }
}
