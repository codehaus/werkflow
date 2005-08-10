package org.codehaus.werkflow.simple;

import java.net.URL;
import java.io.File;

import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.helpers.MockActionManager;

/*
 * $Id$
 */
public class SimpleWorkflowReaderTest
    extends SimpleTestBase
{

    private ActionManager actionManager;
    private ExpressionFactory expressionFactory;

    protected void setUp() throws Exception {

        actionManager = new MockActionManager();
        expressionFactory = new MockExpressionFactory();

    }

    protected void tearDown() throws Exception {

        actionManager = null;
        expressionFactory = null;

    }

    public void testReadFromFile()
        throws Exception
    {

        File file = new File( "target/test-classes/org/codehaus/werkflow/simple/valid.xml" );

        Workflow workflow = SimpleWorkflowReader.read(
            actionManager,
            expressionFactory,
            file );

        assertNotNull( workflow );

    }

    public void testReadFromInputStream()
        throws Exception
    {

        Workflow workflow = SimpleWorkflowReader.read(
            actionManager,
            expressionFactory,
            getClass().getResourceAsStream( "valid.xml" ) );

        assertNotNull( workflow );

    }

    public void testReadFromURL()
        throws Exception
    {

        File file = new File( "target/test-classes/org/codehaus/werkflow/simple/valid.xml" );
        URL url = file.toURL();

        Workflow workflow = SimpleWorkflowReader.read(
            actionManager,
            expressionFactory,
            url);

        assertNotNull( workflow );

    }

    public void testReadFromInputSource()
        throws Exception
    {

        Workflow workflow = SimpleWorkflowReader.read(
            actionManager,
            expressionFactory,
            getClass().getResource( "valid.xml" ) );

        assertNotNull( workflow );

    }

}
