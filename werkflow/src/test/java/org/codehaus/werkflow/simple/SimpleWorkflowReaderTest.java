package org.codehaus.werkflow.simple;

import org.codehaus.werkflow.spi.Instance;
import org.codehaus.werkflow.Workflow;

import java.util.Properties;

public class SimpleWorkflowReaderTest
    extends SimpleTestBase
    implements ActionManager
{
    public void testValidWorkflow()
        throws Exception
    {
        MockExpressionFactory exprFactory = new MockExpressionFactory();

        Workflow workflow = SimpleWorkflowReader.read( this,
                                                       exprFactory,
                                                       getClass().getResource( "valid.xml" ) );

        assertNotNull( workflow );
        
    }

    public void perform(String actionId,
                        Instance instance,
                        Properties props)
        throws Exception
    {
        
    }
}
